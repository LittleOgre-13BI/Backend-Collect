from db.my_mysql import Session


def search_fetchmission_mids_by_uid(uid):
    session = Session()
    sql = 'SELECT mid FROM fetchmission WHERE uid =' + str(uid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    result = [i[0] for i in result]
    return result


def search_fetchmission_picture1_by_fid(fid):
    session = Session()
    sql = 'SELECT picture1, coordinate1 FROM fetchmission WHERE fid =' + str(fid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return (result[0][0],result[0][1])


def search_fetchmission_picture2_by_fid(fid):
    session = Session()
    sql = 'SELECT picture2, coordinate2 FROM fetchmission WHERE fid =' + str(fid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return (result[0][0],result[0][1])


def search_fetchmission_picture3_by_fid(fid):
    session = Session()
    sql = 'SELECT picture3, coordinate3 FROM fetchmission WHERE fid =' + str(fid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return (result[0][0],result[0][1])


def search_fetchmission_picture4_by_fid(fid):
    session = Session()
    sql = 'SELECT picture4, coordinate4 FROM fetchmission WHERE fid =' + str(fid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return (result[0][0],result[0][1])


def search_score(uid, mid):
    session = Session()
    sql = 'SELECT score FROM fetchmission WHERE uid =' + str(uid) + ' and mid =' + str(mid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result[0][0]


def search_all_contents(mid):
    session = Session()
    sql = 'SELECT fid,bug_description,bug_recurrent_steps,device_information FROM fetchmission WHERE mid = ' + str(mid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result

def search_report_content(fid):
    session = Session()
    sql = 'SELECT bug_description,bug_recurrent_steps FROM fetchmission WHERE fid = ' + str(fid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result[0][0] + result[0][1]

# print(search_all_contents(1))
# print(search_fetchmission_mids_by_uid(1))
# print(search_score(2, 1))