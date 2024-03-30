import mysql.connector

def dbconfig():
    db = mysql.connector.connect(
        host = 'localhost',
        user = 'root',
        passwd = 'snehashish'
    )

    return db