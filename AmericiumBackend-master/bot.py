from nextcord import Embed
import nextcord
from nextcord.ext import commands
from dotenv import load_dotenv
import os
import asyncio
import time
import framework
import random
import string
import requests
from bs4 import BeautifulSoup

DISCORD_TOKEN = "ODUzNTAyMDY2MzMxODc3NDA3.YMWTvQ.nrQizDATmBlVt0uYx2zU4mrf3Og"

intents = nextcord.Intents.default()
intents.members = True

bot = commands.Bot(command_prefix='!', intents=intents)

#up

@bot.command(pass_context=True)
async def genkey(ctx, arg=1):
    if not framework.is_admin(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You can't do that!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if ctx.channel.id != 883594817823727634:
        embed = Embed(title="Americium",
                      description="Wrong channel! Use <#883594817823727634>",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    current_keys = []
    for key_id in range(int(arg)):
        key = framework.gen_key(ctx.author.id)
        key = key["key"]
        current_keys.append(f'`{key}`\n')
    # Begin asking questions in DM's
    godhelpme = ''.join(current_keys)

    embed = Embed(title="Americium",
                  description=f'''
                  Generated {len(current_keys)} {"key" if len(current_keys) < 2 else "keys"}: 
                    {godhelpme}
                  ''',
                  color=0x00ff00)
    embed.set_footer(text="Backend made with love by Fluyd")
    await ctx.reply(embed=embed)


@bot.command()
async def checkkey(ctx, arg):
    if not framework.is_admin(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You can't do that!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if ctx.channel.id != 883594817823727634:
        embed = Embed(title="Americium",
                      description="Wrong channel! Use <#883594817823727634>",
                      color=0xe34944)
        await ctx.reply(embed=embed)
        return

    key = framework.check_key(arg)
    if key is None:
        embed = Embed(title="Americium",
                      description="Thats not a valid key!",
                      color=0xe34944)
        await ctx.reply(embed=embed)
        return
    # Begin asking questions in DM's
    embed = Embed(title="Americium",
                  description=f'''
                  Key Info:
                      > `Key`: **{key["key"]}**
                      > `Created By`: **{key["createdBy"]}**
                      > `Used By`: **{"Key has not been used!" if key["usedBy"] == "null" else key["usedBy"]}**
                  ''',
                  color=0x00ff00)
    await ctx.reply(embed=embed)


@bot.command()
async def allkeys(ctx):
    if not framework.is_admin(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You can't do that!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if ctx.channel.id != 883594817823727634:
        embed = Embed(title="Americium",
                      description="Wrong channel! Use <#883594817823727634>",
                      color=0xe34944)
        await ctx.reply(embed=embed)
        return

    keys_all = framework.key_refresh()

    keys = []

    for key in keys_all:
        keys.append(key["key"])

    godhelpme = f'\n > `Key`: '.join(keys)

    # Begin asking questions in DM's
    embed = Embed(title="Americium",
                  description=f'''
                  Keys: 
                    {godhelpme}
                  ''',
                  color=0x00ff00)
    await ctx.reply(embed=embed)


@bot.command()
async def setaccounts(ctx, *args):
    if not framework.check_has_americium(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You dont own Americium!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    framework.set_accounts(args, ctx.author.id)

    # Begin asking questions in DM's
    embed = Embed(title="Americium",
                  description=f'Set your {"account" if len(args) < 2 else "accounts"} to be `{args[0] if len(args) < 2 else ", ".join(args)}`',
                  color=0x00ff00)
    await ctx.reply(embed=embed, delete_after=2)
    await ctx.message.delete()


@bot.command()
async def redeemkey(ctx, arg):
    if framework.check_key(arg) is None:
        embed = Embed(title="Americium",
                      description="Thats not a valid key!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if framework.check_has_americium(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You already own Americium!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if framework.check_key(arg)["usedBy"] != None:
        embed = Embed(title="Americium",
                      description="That key has already been redeemed!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    framework.activate_americium(ctx.author.id, arg)
    member_role = nextcord.utils.get(ctx.guild.roles, id=883594817190371353)
    await ctx.author.add_roles(member_role)

    # Begin asking questions in DM's
    dm = await ctx.author.create_dm()
    embed = Embed(title="Americium",
                  description=f'You now have Americium! Use `!setaccounts [IGN] <Optional: IGN2 IGN3>` in the Americium server to get started!',
                  color=0x00ff00)
    await dm.send(embed=embed)


@bot.command()
async def invitewave(ctx, arg):
    if not framework.is_admin(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You can't do that!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if ctx.channel.id != 883594817823727634:
        embed = Embed(title="Americium",
                      description="Wrong channel! Use <#883594817823727634>",
                      color=0xe34944)
        await ctx.reply(embed=embed)
        return

    for americiumUser in framework.get_all_americium_users():
        user = bot.get_user(americiumUser)
        ukeys = []
        for _ in range(int(arg)):
            key = framework.gen_key(ctx.author.id)
            key = key["key"]
            ukeys.append(key)
        joined = "\n > ".join(ukeys)
        embed = Embed(title="Americium",
                      description=f'A wild invite wave appeared! Here are your keys, give them to whoever you want! \n > **{key if len(ukeys) < 2 else joined}**',
                      color=0x00ff00)
        embed.set_footer(text="Backend made with love by Fluyd")
        await user.send(embed=embed)

    # Begin asking questions in DM's
    embed = Embed(title="Americium",
                  description=f'Send everyone that owns Americium `{arg}` key(s)!',
                  color=0x00ff00)
    embed.set_footer(text="Backend made with love by Fluyd")
    await ctx.reply(embed=embed)


@bot.command()
async def nukegen(ctx):
    if not framework.is_admin(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You can't do that!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    chan = bot.get_channel(932061591598420019)
    await chan.purge()

    # Begin asking questions in DM's
    embed = Embed(title="Americium",
                  description=f'Kaboom!',
                  color=0x00ff00)
    await ctx.reply(embed=embed)


@bot.command()
async def delunusedkeys(ctx):
    if not framework.is_admin(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You can't do that!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if ctx.channel.id != 883594817823727634:
        embed = Embed(title="Americium",
                      description="Wrong channel! Use <#883594817823727634>",
                      color=0xe34944)
        await ctx.reply(embed=embed)
        return

    kamout = framework.delete_unused_keys()

    # Begin asking questions in DM's
    embed = Embed(title="Americium",
                  description=f'Deleted {kamout} unused keys!',
                  color=0x00ff00)
    embed.set_footer(text="Backend made with love by Fluyd")
    await ctx.reply(embed=embed)


@bot.command()
async def findservers(ctx):
    if not framework.is_admin(ctx.author.id):
        embed = Embed(title="Americium",
                      description="You can't do that!",
                      color=0xe34944)
        embed.set_footer(text="Backend made with love by Fluyd")
        await ctx.reply(embed=embed)
        return

    if ctx.channel.id != 883594817823727634:
        embed = Embed(title="Americium",
                      description="Wrong channel! Use <#883594817823727634>",
                      color=0xe34944)
        await ctx.reply(embed=embed)
        return

    def get_mcsl():
        serversz = []
        URL = "https://minecraft-server-list.com/new/"
        page = requests.get(URL)

        soup = BeautifulSoup(page.content, "html.parser")

        results = soup.find("tbody")

        server_addresses = results.find_all("td", class_="n3")

        for address in server_addresses:
            server_ip = address.find("div", class_="adressen")
            players = address.find("span")
            server_ip = server_ip.text.strip()
            players_online = players.text.strip().split('/')
            players_online = players_online[0].split(":")
            players_online = players_online[1].strip()

            try:
                server_data = requests.get(f'https://api.mcsrvstat.us/2/{server_ip}').json()
                server_version = server_data["version"]
            except:
                pass

            if players_online != "":
                serversz.append(
                    f"\n > Server IP: **`{server_ip}`** | Players Online: **{players_online}** | Version: **{server_version}**")
        return serversz

    def get_mcmp():
        serversz = []
        URL = "https://minecraft-mp.com/servers/latest/"
        page = requests.get(URL)

        soup = BeautifulSoup(page.content, "html.parser")

        results = soup.find("tbody")

        server_addresses = results.find_all("tr")

        for idx, address in enumerate(server_addresses):

            server_ip_tmp = address.find("td", class_="")
            server_ip = server_ip_tmp.find("strong")
            server_ip = server_ip.text.strip()

            if server_ip != "Private Server":
                players = address.findAll("td")[2]

                players_online = players.text.strip().split('/')
                players_online = players_online[0]
                try:
                    server_data = requests.get(f'https://api.mcsrvstat.us/2/{server_ip}').json()
                    server_version = server_data["version"]
                except:
                    pass

                if players_online != "N":
                    serversz.append(
                        f"\n > Server IP: **`{server_ip}`** | Players Online: **{players_online}** | Version: **{server_version}**")
        return serversz

    mcmp_servers = get_mcmp()
    mcsl_servers = get_mcsl()

    godhelpme = ''.join(mcmp_servers)
    godhelpmev2 = ''.join(mcsl_servers)

    # Begin asking questions in DM's b
    embed = Embed(title="Americium",
                  description=f'''
                  Found {len(mcmp_servers)} servers! Here they are.
                  -----------------------------------------------------
                  MCMP Servers:
                    {godhelpme}
                  ''',
                  color=0x00ff00)
    embed.set_footer(text="Backend made with love by Fluyd")
    await ctx.reply(embed=embed)
    embed = Embed(title="Americium",
                  description=f'''
                     Found {len(mcsl_servers)} servers! Here they are.
                     -----------------------------------------------------
                     MCSL Servers:
                       {godhelpmev2}
                     ''',
                  color=0x00ff00)
    embed.set_footer(text="Backend made with love by Fluyd")
    await ctx.reply(embed=embed)


@bot.event
async def on_message(message):
    if message.channel.id == 883594817211367449:
        await message.delete()
    await bot.process_commands(message)


bot.run(DISCORD_TOKEN)
