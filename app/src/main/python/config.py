import hashlib
import random
import string
from dbconfig import dbconfig


def generateDeviceSecret(length=10):
    return "".join(
        random.choices(
            string.ascii_uppercase + string.digits, k=length
        )
    )


def config(master):
    db = dbconfig()
    cursor = db.cursor()

    cursor.execute("CREATE DATABASE pm")

    # Create tables
    query = "CREATE TABLE pm.secrets (Masterkey_hash TEXT NOT NULL, Device_secret TEXT NOT NULL)"
    res = cursor.execute(query)

    query = "CREATE TABLE pm.entries (Website_name TEXT NOT NULL, website_url TEXT NOT NULL, email TEXT, username TEXT, password TEXT NOT NULL)"
    res = cursor.execute(query)

    mp = master

    # Hash the MASTER PASSWORD
    hashed_mp = hashlib.sha256(mp.encode()).hexdigest()

    # Generate DEVICE SECRET
    ds = "".join(
        random.choices(
            string.ascii_uppercase + string.digits, k=10
        )
    )

    # add mp and ds to 'secrets' database
    query = "INSERT INTO pm.secrets (Masterkey_hash, Device_secret) VALUES (%s, %s)"
    val = (hashed_mp, ds)
    cursor.execute(query, val)
    db.commit()

    db.close()
