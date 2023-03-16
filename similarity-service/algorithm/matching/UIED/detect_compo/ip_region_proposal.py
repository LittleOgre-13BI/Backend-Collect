from os.path import join as pjoin
import time
from .lib_ip import ip_preprocessing as pre
from .lib_ip import ip_draw as draw
from .lib_ip import ip_detection as det
from .lib_ip import file_utils as file
from .lib_ip import Component as Compo
from ..config.CONFIG_UIED import Config
C = Config()


def nesting_inspection(org, grey, compos, ffl_block):
    '''
    Inspect all big compos through block division by flood-fill
    :param ffl_block: gradient threshold for flood-fill
    :return: nesting compos
    '''
    nesting_compos = []
    for i, compo in enumerate(compos):
        if compo.height > 50:
            replace = False
            clip_grey = compo.compo_clipping(grey)
            n_compos = det.nested_components_detection(clip_grey, org, grad_thresh=ffl_block, show=False)
            Compo.cvt_compos_relative_pos(n_compos, compo.bbox.col_min, compo.bbox.row_min)

            for n_compo in n_compos:
                if n_compo.redundant:
                    compos[i] = n_compo
                    replace = True
                    break
            if not replace:
                nesting_compos += n_compos
    return nesting_compos


def compo_detection(input_img_path, output_root, uied_params,
                    resize_by_height=800, classifier=None, show=False, wai_key=0):

    start = time.clock()
    name = input_img_path.split('/')[-1][:-4] if '/' in input_img_path else input_img_path.split('\\')[-1][:-4]
    ip_root = file.build_directory(pjoin(output_root, "ip"))

    # *** Step 1 *** pre-processing: read img -> get binary map
    org, grey = pre.read_img(input_img_path, resize_by_height)
    binary = pre.binarization(org, grad_min=int(uied_params['min-grad']))

    # *** Step 2 *** element detection
    det.rm_line(binary, show=show, wait_key=wai_key)
    uicompos = det.component_detection(binary, min_obj_area=int(uied_params['min-ele-area']))

    # *** Step 3 *** results refinement
    uicompos = det.compo_filter(uicompos, min_area=int(uied_params['min-ele-area']), img_shape=binary.shape)
    uicompos = det.merge_intersected_compos(uicompos)
    det.compo_block_recognition(binary, uicompos)
    if uied_params['merge-contained-ele']:
        uicompos = det.rm_contained_compos_not_in_block(uicompos)
    Compo.compos_update(uicompos, org.shape)
    Compo.compos_containment(uicompos)

    # *** Step 4 ** nesting inspection: check if big compos have nesting element
    uicompos += nesting_inspection(org, grey, uicompos, ffl_block=uied_params['ffl-block'])
    Compo.compos_update(uicompos, org.shape)
    draw.draw_bounding_box(org, uicompos, show=show, name='merged compo', write_path=pjoin(ip_root, name + '.jpg'), wait_key=wai_key)

    # *** Step 5 *** element classification: all category classification
    if classifier is not None:
        classifier['Elements'].predict([compo.compo_clipping(org) for compo in uicompos], uicompos)
        draw.draw_bounding_box_class(org, uicompos, show=show, name='cls', write_path=pjoin(ip_root, 'result.jpg'))
        draw.draw_bounding_box_class(org, uicompos, write_path=pjoin(output_root, 'result.jpg'))

    # *** Step 6 *** save detection result
    Compo.compos_update(uicompos, org.shape)
    file.save_corners_json(pjoin(ip_root, name + '.json'), uicompos)
    print("[Compo Detection Completed in %.3f s] Input: %s Output: %s" % (time.clock() - start, input_img_path, pjoin(ip_root, name + '.json')))
