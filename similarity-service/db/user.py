from db.my_mysql import Session

def search_all_user():
    session = Session()
    sql = 'SELECT * FROM user WHERE user_type = "employee" '
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result

def search_user_devices(uid):
    session = Session()
    sql = "SELECT user.devices FROM user WHERE user.uid =" + str(uid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    if result[0][0] is None:
        return []
    return result[0][0].split(',')
    # return result

def search_all_uids():
    session = Session()
    sql = 'SELECT uid FROM user WHERE user_type = "employee"'
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return [i[0] for i in result]

def search_reputation(uid):
    session = Session()
    sql = "SELECT reputation FROM user WHERE user.uid =" + str(uid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    return result[0][0]

def search_user_labels(uid):
    session = Session()
    sql = "SELECT labels FROM user WHERE user.uid =" + str(uid)
    cursor = session.execute(sql)
    result = cursor.fetchall()
    session.close()
    if result[0][0] is None:
        return []
    return result[0][0].split(',')

# print(search_user_devices(2))
# print(search_all_uids())
# print(search_reputation(2))
# print(search_labels(1))