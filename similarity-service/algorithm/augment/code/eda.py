import random
from random import shuffle
import synonyms as sy
import  os
import  jieba
random.seed(1)

#加载停用词表
stop_words = []
path = os.path.dirname(__file__)
with open(path +"/stopwords_zh", "r", encoding="utf-8") as f:
    for line in f:
        stop_words.append(line[:-1])

#文本清洗
import re


def get_only_chars(line):

    clean_line = ""

    line = line.replace("’", "")
    line = line.replace("'", "")
    line = line.replace("-", " ")  #replace hyphens with spaces
    line = line.replace("\t", " ")
    line = line.replace("\n", " ")
    line = line.lower()

    clean_line = line

    clean_line = re.sub(' +', ' ', clean_line)  #delete extra spaces
    if clean_line[0] == ' ':
        clean_line = clean_line[1:]
    return clean_line


########################################################################
# 同义替换
########################################################################


def synonym_replacement(words, n):
    new_words = words.copy()
    random_word_list = list(
        set([word for word in words if word not in stop_words]))
    random.shuffle(random_word_list)
    num_replaced = 0
    for random_word in random_word_list:
        synonyms = get_synonyms(random_word)
        if len(synonyms) >= 1:
            synonym = random.choice(list(synonyms))
            new_words = [
                synonym if word == random_word else word for word in new_words
            ]
            num_replaced += 1
        if num_replaced >= n:  #最多替换n个词
            break

    sentence = ' '.join(new_words)
    new_words = sentence.split(' ')

    return new_words


def get_synonyms(word):
    synonyms_cadidate = set()

    for sy_word in sy.nearby(word)[0]:
        synonyms_cadidate.add(sy_word)
    if word in synonyms_cadidate:
        synonyms_cadidate.remove(word)
    return list(synonyms_cadidate)


########################################################################
# 随机删除
########################################################################


def random_deletion(words, p):

    #只剩一个单词不要删除
    if len(words) == 1:
        return words

    new_words = []
    for word in words:
        r = random.uniform(0, 1)
        if r > p:
            new_words.append(word)

    #如果删空了，就随机返回一个
    if len(new_words) == 0:
        rand_int = random.randint(0, len(words) - 1)
        return [words[rand_int]]

    return new_words


########################################################################
# 随机交换 n 次
########################################################################


def random_swap(words, n):
    new_words = words.copy()
    for _ in range(n):
        new_words = swap_word(new_words)
    return new_words


def swap_word(new_words):
    random_idx_1 = random.randint(0, len(new_words) - 1)
    random_idx_2 = random_idx_1
    counter = 0
    while random_idx_2 == random_idx_1:
        random_idx_2 = random.randint(0, len(new_words) - 1)
        counter += 1
        if counter > 3:
            return new_words
    new_words[random_idx_1], new_words[random_idx_2] = new_words[
        random_idx_2], new_words[random_idx_1]
    return new_words


########################################################################
# 随机插入 n 个单词
########################################################################


def random_insertion(words, n):
    new_words = words.copy()
    for _ in range(n):
        add_word(new_words)
    return new_words


def add_word(new_words):
    synonyms = []
    counter = 0
    while len(synonyms) < 1:
        random_word = new_words[random.randint(0, len(new_words) - 1)]
        synonyms = get_synonyms(random_word)
        counter += 1
        if counter >= 10:
            return
    random_synonym = synonyms[0]
    random_idx = random.randint(0, len(new_words) - 1)
    new_words.insert(random_idx, random_synonym)


########################################################################
# 主方法
########################################################################


def eda(sentence,
        alpha_sr,
        alpha_ri,
        alpha_rs,
        p_rd,
        num_aug):

    sentence = get_only_chars(sentence)
    words = sentence.split(' ')
    words = [word for word in words if word is not '']
    num_words = len(words)

    augmented_sentences = []
    num_new_per_technique = int(num_aug / 4) + 1
    n_sr = max(1, int(alpha_sr * num_words))
    n_ri = max(1, int(alpha_ri * num_words))
    n_rs = max(1, int(alpha_rs * num_words))

    #注：理论上应该四种技术扩增相同次数，但是其它三种的结果太过于离谱，因此同义替换多做了10倍的次数。
    #sr
    for _ in range(num_new_per_technique * 10):
        a_words = synonym_replacement(words, n_sr)
        augmented_sentences.append(' '.join(a_words))

    #ri
    for _ in range(num_new_per_technique):
        if len(words) < 3:
            continue
        a_words = random_insertion(words, n_ri)
        augmented_sentences.append(' '.join(a_words))

    #rs
    for _ in range(num_new_per_technique):
        if len(words) < 3:
            continue
        a_words = random_swap(words, n_rs)
        augmented_sentences.append(' '.join(a_words))

    #rd
    for _ in range(num_new_per_technique):
        if len(words) < 3:
            continue
        a_words = random_deletion(words, p_rd)
        augmented_sentences.append(' '.join(a_words))

    augmented_sentences = [
        get_only_chars(sentence) for sentence in augmented_sentences
    ]
    shuffle(augmented_sentences)

    #取需要数目的新扩增的字段
    augmented_sentences = augmented_sentences[:num_aug]

    return augmented_sentences


def strAugment(input):
    # alpha决定句子修改的幅度。
    alpha = 0.1
    words = jieba.cut(input)
    processed_input = ""
    for word in words:
        processed_input = processed_input + word + " "
    # outputs: list类型，为 num_aug 个新扩增出的字段。
    outputs = eda(processed_input,
                 alpha_sr=alpha,
                 alpha_ri=alpha,
                 alpha_rs=alpha,
                 p_rd=alpha,
                 num_aug=1)
    #只返回去掉空格后的新扩增字段
    return outputs[0].replace(" ", "")


def reportAugment(input1, input2, input3, input4):
    output1 = strAugment(input1)
    output2 = strAugment(input2)
    output3 = strAugment(input3)
    output4 = strAugment(input4)
    outputs = []
    outputs.append(output1)
    outputs.append(output2)
    outputs.append(output3)
    outputs.append(output4)
    return outputs
