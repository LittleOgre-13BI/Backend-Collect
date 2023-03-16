from db.my_mysql import Session

# def search_all_mids():
#     session = Session()
#     sql = "SELECT mid FROM mission"
#     cursor = session.execute(sql)
#     result = cursor.fetchall()
#     session.close()
#     return [i[0] for i in result]

def search_all_mission_mid_and_device():
    session = Session()
    sql = "SELECT mid,device_req FROM mission WHERE state = 'Recruiting'"
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result

def search_all_mission_mid_and_device():
    session = Session()
    sql = "SELECT mid,device_req FROM mission"
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result

def search_all_recruiting_mission_mid_and_device():
    session = Session()
    sql = "SELECT mid,device_req FROM mission WHERE state = 'Recruiting'"
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result

def search_mission_mid_and_url(mid):
    session = Session()
    print(mid)
    sql = "SELECT mid, doc_url FROM mission WHERE mid = " + str(mid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    # print(result)
    return (result[0][0],result[0][1])

def search_difficulty(mid):
    session = Session()
    sql = "SELECT difficulty_level FROM mission WHERE mid =" + str(mid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result[0][0]

def search_mission_labels(mid):
    session = Session()
    sql = "SELECT labels FROM mission WHERE mid =" + str(mid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    if result[0][0] is None:
        return []
    return result[0][0].split(',')

# print(search_all_mission_mid_and_device())
# print(search_mission_mid_and_url(1))
# print(search_all_mids())
# print(search_difficulty(1))
# print(search_labels(2))