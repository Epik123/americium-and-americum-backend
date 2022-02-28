<<<<<<< HEAD
from elements import elements
import uuid
import elements.elements as elements

import pymongo
from fastapi import FastAPI
=======
import os
import time
import random
from elements import elements
import uuid
import elements.elements as elements
import nextcord

import pymongo
import string
from fastapi import FastAPI, HTTPException, File, UploadFile
>>>>>>> a487678 (Inittest)

app = FastAPI()

# MongoDB Connection
myclient = pymongo.MongoClient("mongodb+srv://americiumcodedb:jPt6uBz5egHpSiF@americiumdb.hab19.mongodb.net")
mydb = myclient["Americium"]
mycol = mydb["Keys"]
myusers = mydb["Users"]

def gen_key(createBy):
    genedkey = {"key": str(uuid.uuid4()), "createdBy": createBy, "usedBy": None}
    x = mycol.insert_one(genedkey)
    return genedkey

def key_refresh():
    keys = []
    for x in mycol.find():
        keys.append(x)
    keyss = []
    for key in keys:
        keyss.append(key)
    return keyss

def check_key(key):
    for x in mycol.find({ "key": key }):
        return x

def is_admin(user_id):
    for x in myusers.find({"discordID": int(user_id) }):
        if x["admin"]:
            return True
        else:
            return False

def is_admin_mc(minecraft_username):
    users = []
    for x in myusers.find():
        users.append(x)
    admins = []
    user_names = []
    print(users)
    for user in users:
        if user["admin"]:
            for account in user["accounts"]:
                user_names.append(account.lower())
    return True if minecraft_username in user_names else False

def is_youtuber_mc(minecraft_username):
    users = []
    for x in myusers.find():
        users.append(x)
    youtubers = []
    user_names = []
    print(users)
    for user in users:
        if user["youtube"]:
            for account in user["accounts"]:
                user_names.append(account.lower())
    return True if minecraft_username in user_names else False

def check_user(minecraft_username):
    users = []
    for x in myusers.find():
        users.append(x)
    user_names = []
    print(users)
    for user in users:
        for account in user["accounts"]:
            user_names.append(account.lower())
    return True if minecraft_username in user_names else False

def set_accounts(accounts, discord_id):
    myusers.update_one({'discordID': discord_id}, {'$set': {'accounts': []}})
    for account in accounts:
        myusers.update_one({'discordID': discord_id}, {'$push': {'accounts': account}})

def check_has_americium(discord_id):
    for x in myusers.find({"discordID": int(discord_id) }):
        if x:
            return True
        else:
            return False

def activate_americium(discord_id, key):
    new_user = {"discordID": discord_id, "accounts": [], "admin": False, "youtube": False}
    x = myusers.insert_one(new_user)
    mycol.update_one({'key': key}, {'$set': {'usedBy': discord_id}})
    return new_user

def get_all_americium_users():
    users = []
    for x in myusers.find():
        users.append(x["discordID"])
    return users

def delete_unused_keys():
    myquery = {"usedBy": None}
    x = mycol.delete_many(myquery)
    return x.deleted_count
# keys = key_refresh()
# print(keys)
# print("Finished key check!")
# print(check_key("25308050-dfa1-42a2-9424-00b21a213fdb"))
# print("Finished get key!")
# print(gen_key(720267178288611400))

#CREATE THE DISCORD BOT#