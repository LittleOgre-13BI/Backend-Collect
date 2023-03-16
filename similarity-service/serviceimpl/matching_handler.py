from db import fetchmission
from algorithm.matching.UIED.main import execute


def calMatching(fid, lda_news):
    count = 0
    total = 0.0

    text = fetchmission.search_report_content(fid)

    pic = fetchmission.search_fetchmission_picture1_by_fid(fid)
    url = pic[0]
    if url is not None:
        if len(url) > 0:
            pos_str = str(pic[1]).split(",")
            pos = []
            for i in range(0, 4):
                pos.append(int(pos_str[i]))
            total += execute(text, url, pos[0], pos[1], pos[2], pos[3], lda_news)
            count += 1
        else:
            return 0
    else:
        return 0

    pic = fetchmission.search_fetchmission_picture2_by_fid(fid)
    url = pic[0]
    if url is not None:
        if len(url) > 0:
            pos_str = str(pic[1]).split(",")
            pos = []
            for i in range(0, 4):
                pos.append(int(pos_str[i]))
            total += execute(text, url, pos[0], pos[1], pos[2], pos[3], lda_news)
            count += 1
        else:
            return total / count
    else:
        return total / count

    pic = fetchmission.search_fetchmission_picture3_by_fid(fid)
    url = pic[0]
    if url is not None:
        if len(url) > 0:
            pos_str = str(pic[1]).split(",")
            pos = []
            for i in range(0, 4):
                pos.append(int(pos_str[i]))
            total += execute(text, url, pos[0], pos[1], pos[2], pos[3], lda_news)
            count += 1
        else:
            return total / count
    else:
        return total / count

    pic = fetchmission.search_fetchmission_picture4_by_fid(fid)
    url = pic[0]
    if url is not None:
        if len(url) > 0:
            pos_str = str(pic[1]).split(",")
            pos = []
            for i in range(0, 4):
                pos.append(int(pos_str[i]))
            total += execute(text, url, pos[0], pos[1], pos[2], pos[3], lda_news)
            count += 1
        else:
            return total / count
    else:
        return total / count

    return total / count