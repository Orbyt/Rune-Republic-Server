package com.rs.worldserver.model.player;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;


/**
 * Clue handler
 * 
 * @author Chachi
 * 
 */
public class ClueAssistant {
	private Client client;
	
	public ClueAssistant(Client client) {
		this.client = client;
	}
	
	// Armor
    public static int Reward1[] = { 
	2577,2579,2581,2583,2585,2587,2589,2591,2593,2595,2597,2599,2601,2603,2605,2607,2609,2611,2613,2615,2617,2619,
	2621,2623,2625,2627,2629,2631,2633,2635,2637,2639,2641,2643,2645,2647,2649,2651,2653,2654,2656,2657,2659,2661,
	2663,2665,2667,2669,2671,2673,2675,3478,3479,3480,3481,3483,3485,3486,3488,7319,7321,7323,7325,7327,7332,7334,
	7336,7338,7340,7342,7344,7346,7348,7350,7352,7354,7356,7358,7360,7362,7364,7366,7368,7370,7372,7374,7376,7378,
	7380,7382,7384,7398,7399,7400,7498,7597,7598,7599,7600,7601,7602,7603,7604,7605,7606,7607,7608,10330,10338,10348,10332,
	2858,10334,10342,10350,10344,10352,10340,10336,18706,18707,4506,4507,4509,4510,4511,4512,5553,5554,5555,5556,5557,4069,4070,4071,4072,
	4037,4039,19311,19368,19370,19372,15352,19893,19748,19459,19451,19443,19440,19425,19410,19314,19465,19457,19449,19437,19422,19407,9920,10728,
	11790,10723,19392,19394,19396,19317,19263,19257,19251,19242,19236,19230,19221,19215,19209,19200,19194,19188,19179,19173,19167,19461,19453,
	19445,19428,19413,19398,19380,19382,19384,19320,19266,19269,19259,19261,19253,19255,19248,19245,19238,19240,19232,19234,19224,19227,19217,
	19219,19211,19213,19203,19206,19196,19198,19190,19192,19182,19185,19175,19177,19169,19171,19463,19455,19447,19431,19434,19416,19419,19401,
	19404,19386,19388,19390,19272,19275,19278,19323,19325,19327,19329,19331,13101,6656,
	19613,19615,19617,10394,10396,10400,10402,10404,10406,10408,10410,10412, 10414, 10416,10418,10420,10422,10424,
	10426,10428,10430,10432,10434,10436,10438,10346

	};
	
		// Food
	public static int Reward3[] = { 378, 380, 384, 386, 390, 392, 11212};
	// Runes (drag arrows added)
	public static int Reward4[] = { 9075, 554, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 995 };
	// Rune Armor & Dragon Weapons
	public static int Reward5[] = { 9185, 892, 1079, 1093, 1113, 1127, 1163, 1185, 1201, 1149, 1187, 1249, 1305, 1377, 1434, 11732, 3204, 4587, 4675, 5698 };
	// Potions
	public static int Reward6[] = { 537, 2437, 2441, 2443, 2451, 3025, 6686,15308,15312,15316,15320,15324,15332 };
	// Xmas event rewards below
	public static int Reward7[] = { 4079,14600,14601,14602,14603,14604,14605,15426,10507};
	
	public static int ClueScroll[] = { 2841, 2843, 2845, 2847, 2848, 2849, 2851, 2853, 2855, 2856, 2857, 2858 };

	public static String Clue[] = { "Dig at the exact spot near King Black Drag entrance", 
	"Dig at the exact spot at Mage Bank", "Dig at the exact spot near Taverly", 
	"Dig at the exact spot at Greater Demons", "Dig at the exact spot near Black Dragons", 
	"Dig at the exact spot near Axe Hut", "Dig at the exact spot near Varrock Castle", 
	"Dig at the exact spot near Graveyard Dragons","Dig at the exact spot in Godwars Dungeon",
	"Dig at the exact spot in Dagganoth Dungeon","Dig at the exact spot in Level 11 Wild",
	"Dig at the exact spot in the city of Falador","Dig at the exact spot in Level 17 Wild",
	"Dig at the exact spot near Waterfall","Dig at the exact spot near Canfis",
	"Dig at the exact spot near Ardougne","Dig at the exact spot near Coal Trucks Track",
	"I can see a landing strip from here!","Lumbridge swamp has a fern?"
	};

	public static String randomClueString(){
        return Clue[(int)(Math.random()*Clue.length)];
    }	

	public static int randomReward1(){
        return Reward1[(int)(Math.random()*Reward1.length)];
    }	
	public static int randomReward3(){
        return Reward3[(int)(Math.random()*Reward3.length)];
    }
	public static int randomReward4(){
        return Reward4[(int)(Math.random()*Reward4.length)];
    }	
	public static int randomReward5(){
        return Reward5[(int)(Math.random()*Reward5.length)];
    }	
	public static int randomReward6(){
        return Reward6[(int)(Math.random()*Reward6.length)];
    }	
	public static int randomReward7(){
        return Reward7[(int)(Math.random()*Reward7.length)];
    }
	
	
}
