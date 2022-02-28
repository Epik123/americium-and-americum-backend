import os
import time
import random

import elements.elements
import pymongo
import string
import pathlib

import requests

import framework
from fastapi.responses import PlainTextResponse
from fastapi import FastAPI, HTTPException, File, UploadFile, Request, Form, Request
from fastapi.staticfiles import StaticFiles
from discord_webhook import DiscordWebhook, DiscordEmbed

app = FastAPI()
app.mount("/upload", StaticFiles(directory="uploads"), name="uploads")

#MongoDB Connection
myclient = pymongo.MongoClient("mongodb+srv://americiumcodedb:jPt6uBz5egHpSiF@americiumdb.hab19.mongodb.net")
mydb = myclient["Americium"]
mycol = mydb["Users"]

#Getting all the users from the MongoDB
users = []
for x in mycol.find():
  users.append(x)
user_names = []
print(users)
for user in users:
     for account in user["accounts"]:
         user_names.append(account.lower())
print(user_names)

@app.get("/")
async def root():
    raise HTTPException(status_code=403)


# @app.get("/hello/{name}")
# async def say_hello(name: str):
#     return {"message": f"Hello {name}"

@app.get('/checkgroup/{user}')
async def geck_group(user: str):
    user = user.lower()

    if framework.is_admin_mc(user):
        raise HTTPException(status_code=200, detail="admin")
    else:
        if framework.is_youtuber_mc(user):
            raise HTTPException(status_code=200, detail="youtuber")
        else:
            if framework.check_user(user):
                    raise HTTPException(status_code=200, detail="basic")
            else:
                raise HTTPException(status_code=404)

@app.post("/uploads/file", response_class=PlainTextResponse)
async def receive_file(request: Request, file: UploadFile = File(...)):
    dir_path = os.path.dirname(os.path.realpath(__file__))
    letters = string.ascii_letters + string.digits + string.ascii_uppercase
    fileName = f"{random.choice(elements.elements.AllElements)}-{random.choice(elements.elements.AllElements)}-{random.choice(elements.elements.AllElements)}{pathlib.Path(f'{dir_path}/uploads/{file.filename}').suffix}"
    filename = f'{dir_path}/uploads/{fileName}'
    f = open(f'{filename}', 'wb')
    content = await file.read()
    f.write(content)
    webhook = DiscordWebhook(
        url='https://discord.com/api/webhooks/936802644498997338/_8XE-p4wMZO4yoOOxgE2nfWc7jjstzsA04oWLE7vPls5DxlTvsfBDexNCKSuJ1Mn5Ok-')
    embed = DiscordEmbed(
        title="File Upload", color='36f75a'
    )
    embed.set_footer(text="fluyd is a nigger confirmed")
    # Set `inline=False` for the embed field to occupy the whole lin
    embed.add_embed_field(name="File Name", value=fileName, inline=False)
    embed.add_embed_field(name="URL", value=f'https://upload.americium.systems/upload/{fileName}', inline=False)
    embed.add_embed_field(name="Client IP", value=request.client.host, inline=False)
    f.seek(0, os.SEEK_END)
    embed.add_embed_field(name="File Size", value=f.tell(), inline=False)

    webhook.add_embed(embed)
    webhook.execute()
    return f'https://upload.americium.systems/upload/{fileName}'


@app.get("/version", response_class=PlainTextResponse)
async def version():
    return "1.3.2"

@app.post("/uploads/text", response_class=PlainTextResponse)
async def text_text_upload(request: Request, text: str = Form(...), akey: str = Form(...)):
    if akey != "amerultramoment":
        return
    dir_path = os.path.dirname(os.path.realpath(__file__))
    letters = string.ascii_letters + string.digits + string.ascii_uppercase
    fileName = f"{random.choice(elements.elements.AllElements)}-{random.choice(elements.elements.AllElements)}-{random.choice(elements.elements.AllElements)}.txt"
    filename = f'{dir_path}/uploads/{fileName}'
    f = open(f'{filename}', 'wt')
    f.write(text)

    webhook = DiscordWebhook(url='https://discord.com/api/webhooks/936802644498997338/_8XE-p4wMZO4yoOOxgE2nfWc7jjstzsA04oWLE7vPls5DxlTvsfBDexNCKSuJ1Mn5Ok-')
    embed = DiscordEmbed(
        title="File Upload", color='36f75a'
    )
    embed.set_footer(text="fuck fluyd")
    # Set `inline=False` for the embed field to occupy the whole lin
    embed.add_embed_field(name="File Name", value=fileName, inline=False)
    embed.add_embed_field(name="URL", value=f'https://upload.americium.systems/upload/{fileName}', inline=False)
    embed.add_embed_field(name="Client IP", value=request.client.host, inline=False)
    f.seek(0, os.SEEK_END)
    embed.add_embed_field(name="File Size", value=f.tell(), inline=False)

    webhook.add_embed(embed)
    webhook.execute()
    return f'https://upload.americium.systems/upload/{fileName}'

@app.get("/gen/dynam/key", response_class=PlainTextResponse)
async def generate_dynamic_key(request: Request, americiumauth: str, amt: int = Form(...)):
    if americiumauth != "W78tWXGjhSLyCoTnMCyi":
        raise HTTPException(status_code=403)

    curkeys = []

    for numberinumberate in range(amt):
        discord_key = framework.gen_key(0)
        discord_key = discord_key["key"]
        payload = {'account/invitation': ''}
        headers = {
            "Cookie": "xf_user=2%2CwGVewsB-wWnKCPNkZquR838q2Pj9vbDCW9Py4cKm; xf_csrf=pOdK3J2bu_c0SrQ5; xf_session=rSggeDdMguY7o_seAe640yizvfZzQMII"
        }
        forums_key = requests.request("POST", "https://www.americium.systems/forum/index.php?account/invitation", data={
            "token": "OTWvAZUrrABTAKz5ZX79t_r2g9bQkj9c",
            "_xfWithData": "1",
            "_xfToken": "1643742051,e637e403b424a618a64750a7db174885",
            "_xfResponseType": "json"
        }, headers=headers, params=payload)
        print(forums_key.url)
        print(forums_key.text)
        print(forums_key.json())
        forums_key = forums_key.json()
        forums_key = forums_key["redirect"]
        forums_key = forums_key.split('=')
        forums_key = forums_key[1]
        print(forums_key)
        curkeys.append(f'''Discord Key: {discord_key}
Discord Invite: https://discord.com/invite/dtQFCwsUec
Forum Invite Key: {forums_key}
Forum Link: https://spigotmc.co/forum/''')

    return ',\n'.join(curkeys)
