package com.rs.worldserver.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.rs.worldserver.Server;
/**
 * A collection of item methods.
 * 
 * @author Orbit
 *  
 */
public class Item {

	public int id;
	private int amount;

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Item(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
// hoods 16755,17279,
	// Few item types for equipping
	public static int capes[] = {7954,7950,7951,23844,23852,22298,21407,21408,20771,21406,5607,19311,19368,19370,19372,15352,19893,19748,7979,9798,9799,9801,9802,9804,9805,9807,9808,9810,9811,9813,9948,9949,9789,9790,9792,9793,9795,9796,9777,9778,9780,9781,9783,9784,9786,9787,9747,9748,9750,9751,9753,9754,9756,9757,9759,9760,9762,9763,9765,9766,9768,9769,9771,9772,9774,9775,6959,7950,7942,7943,7944,7890,7879,7882,7884,7886,13656, 13653, 7535, 8102, 8075, 8044, 8045, 8042, 8043, 8037, 8038, 8025, 8026, 8018, 7994, 7983, 7984, 7985, 7986, 7987, 7982, 7978, 3781, 3783, 3785, 3787, 3789, 3777, 3779, 3759, 3761, 3763, 3765, 6111, 6570, 6568, 1007, 1019, 1021, 1023, 1027, 1029, 1031, 1052, 2412, 2413, 2414, 4304, 4315, 4317, 4319, 4321, 4323, 4325, 4327, 4329, 4331, 4333, 4335, 4337, 4339, 4341, 4343, 4345, 4347, 4349, 4351, 4353, 4355, 4357, 4359, 4361, 4363, 4365, 4367, 4369, 4371, 4373, 4375, 4377, 4379, 4381, 4383, 4385, 4387, 4389, 4391, 4393, 4395, 4397, 4399, 4401, 4403, 4405, 4407, 4409, 4411, 4413, 4514, 4516, 10499, 6568, 6570,23659};
    public static int boots[] = {21471,10724,15037,15031,15025,11019,20119,14605,17281,17283,17265,17267,17269,17271,16931,11728,13043,16359,16357,17317,11732,7898,7840,7159, 7114, 7596, 8029, 6619, 8017, 7159, 7991, 6666, 6061, 6106, 88, 89, 626, 628, 630, 632, 634, 1061, 1837, 1846, 2577, 2579, 2894, 2904, 2914, 2924, 2934, 3061, 3105, 3107, 3791, 4097, 4107, 4117, 4119, 4121, 4123, 4125, 4127, 4129, 4131, 4310, 5064, 5345, 5557, 6069, 6106, 6143, 6145, 6147, 6328, 6920, 6349, 6357, 3393,21787,21790,21793};
    public static int gloves[] = {13845,13846,13847,13848,13849,13850,13851,13852,13853,13854,13855,13856,13857,13618,13623,13628,21470,19459,19451,19443,10725,15026,15032,15038,20118,14602,17171,13381,16293,13044,17215,18347,16291,8842,10336,7608, 7604, 7600, 7595, 6629, 8021, 8016, 2491, 1065, 2487, 2489, 3060, 1495, 775, 777, 778, 6708, 1059, 1063, 1065, 1580, 2487, 2489, 2491, 2902, 2912, 2922, 2932, 2942, 3060, 3799, 4095, 4105, 4115, 4308, 5556, 6068, 6110, 6149, 6151, 6153, 6922, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461, 7462, 6330, 3391,22358,22359,22360,22361,22362,22363,22364,22365,22366,22367,22368,22369};
    public static int shields[] = {7953,19345,19440,19425,19410,17285,17287,17273,13395,18359,18346,20072,18361,17361,17359,18363,19340,7346,2621,8844,8845,8846,8847,8848,8849,8850,7935,10352,13744, 13738, 13655, 13740, 13736, 13742, 13734, 13734, 2589, 11283, 7342, 7348, 7354, 7360, 7334, 7340, 7347, 7352, 7358, 7356, 7350, 7344, 8087, 8058, 8059, 8060, 8061, 8062, 8063, 6633, 7977, 7976, 7972, 7959, 6591, 7332, 7338, 7336, 7360, 1171, 1173, 1175, 1177, 1179, 1181, 1183, 1185, 1187, 1189, 1191, 1193, 1195, 1197, 1199, 1201, 1540, 2589, 2597, 2603, 2611, 11283, 2629, 2659, 2667, 2675, 2890, 3122, 3488, 3758, 3839, 3840, 3841, 3842, 3843, 3844, 4072, 4156, 4224, 4225, 4226, 4227, 4228, 4229, 4230, 4231, 4232, 4233, 4234, 4302, 4507, 4512, 6215, 6217, 6219, 6221, 6223, 6225, 6227, 6229, 6231, 6233, 6235, 6237, 6239, 6241, 6243, 6245, 6247, 6249, 6251, 6253, 6255, 6257, 6259, 6261, 6263, 6265, 6267, 6269, 6271, 6273, 6275, 6277, 6279, 6524, 6889};
    public static int hats[] = {22482,21736,13625,13613,13616,13621,13626,20949,20950,20951,14494,19341,9472,13672,21467,20952,20772,9514,9512,9513,9511,19747,15422,20077,13113,13115,13109,13111,13107,13103,13105,19376,19377,19374,10507,13101,9812,9814,9806,9809,9800,9803,9794,9797,9788,9791,9782,9785,9776,9779,9770,9773,9764,9767,9758,9761,9752,9755,9749,19314,19465,19457,19449,19437,19422,19407,9920,10728,11790,10723,20147,20159,15033,15027,15021,11021,14743,14745,14747,14749,14751,14753,14755,14757,14759,14761,14763,14765,14767,14769,14771,14773,14775,14777,14779,14781,14783,14785,14787,14789,14791,20147,20135,20115,16755,7906,3753, 3755, 3749, 3751,19296,13898,19336,16709,11335,16711,11002,2900,4502, 13263, 2524, 2526,9119,7966,17061,19278,19275,19272,19293,19302,19305,19281,19284,19287,19290,19299,4515,7837,7838,7839,2900,9010,13866,13878,10828,9220,10550,13039,10548,15608,9510,9509,9508,9507,9506,9505,9504,9503,9502,9501,9500,14499,15509,6659,7607,7603,7599,1167,11665,7904,7956,7958,7940,11664,11663,10350,10342,10334,11718,7899,7891,7892,7888,13896,13876,13864,7841, 7819, 7821,7823,7824,7825,7826,7827,7828,7829,7830,7831,7832,7833,7834,7835,7834,7835,7836,7837,7838,7839,13263, 2524, 2526, 2518, 7612, 7611, 13659, 13658, 7319, 7321, 7323, 7325, 7327, 8100, 8077, 8076, 8074, 4168, 8034, 8035, 8036, 8030, 6623, 8024, 8023, 8022, 8013, 1169, 7594, 7995, 7996, 7997, 7998, 7999, 8000, 8001, 7992, 7990, 7975, 7973, 7971, 7967, 7963, 6665, 6665, 7321, 6886, 6547, 6548, 2645, 2647, 2649, 4856, 4857, 4858, 4859, 4880, 4881, 4882, 4883, 4904, 4905, 4906, 4907, 4928, 4929, 4930, 4931, 4952, 4953, 4954, 4955, 4976, 4977, 4978, 4979, 4732, 4753, 4611, 6188, 6182, 4511, 4056, 4071, 4724, 2639, 2641, 2643, 2665, 6109, 5525, 5527, 5529, 5531, 5533, 5535, 5537, 5539, 5541, 5543, 5545, 5547, 5549, 5551, 74, 579, 656, 658, 660, 662, 664, 740, 1017, 1037, 1040, 1042, 1044, 1046, 1038,1048, 1050, 1053, 1055, 1057, 1137, 1139, 1141, 1143, 1145, 1147, 1149, 1151, 1153, 1155, 1157, 1159, 1161, 1163, 1165, 1506, 1949, 2422, 2581, 2587, 2595, 2605, 2613, 2619, 2627, 2631,979, 2633, 2635, 2637, 2651, 2657, 2673, 2900, 2910, 2920, 2930, 2940, 2978, 2979, 2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988, 2989, 2990, 2991, 2992, 2993, 2994, 2995, 3057, 3385, 3486, 3748, 3749, 3751, 3753, 3797, 4041, 4042, 4071, 4089, 3755, 4099, 4109, 4164, 4302, 4511, 4513, 4515, 4551, 4567, 4708, 4716, 4724, 4745, 4753, 4856, 4857, 4858, 4859, 4880, 4881, 4882, 4883, 4904, 4905, 4906, 4907, 4952, 4953, 4954, 4955, 4976, 4977, 4978, 4979, 5013, 5014, 5554, 5574, 6109, 6128, 6131, 6137, 6182, 6188, 6335, 6337, 6339, 6345, 6355, 6365, 6375, 6382, 6392, 6400, 6918, 6656, 2581, 7539, 7394, 7396, 7534, 5574, 6885, 6858, 6860, 6862, 6856, 6326, 6128, 6137, 7400, 7323, 7325, 7327, 7003, 18745};
    public static int amulets[] = {17291,9470,6861,19392,19394,19396,7803,19888,18333,18335,15126,19335,18334,15511,6577,6565,3853,10344,10344,7810, 7811, 7812, 1497, 4183, 1654, 1656, 1658, 1660, 1662, 1664, 8081, 8033, 7968, 6585, 86, 87, 295, 421, 552, 589, 1478, 1692, 1694, 1696, 1698, 1700, 1702, 1704, 1706, 1708, 1710, 1712, 1725, 1727, 1729, 1731, 4021, 4081, 4250, 4677, 6040, 6041, 6208, 1718, 1722, 6859, 6863, 6857, 3867};
    public static int arrows[] = {15243,7858,7859,7860,7861,7862,7863,7864,7865,7866,7867,7868,7869,7870,7871,7872,11212, 7618, 8052, 8065, 11718, 7988, 7989, 78, 598, 877, 878, 879, 880, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893, 942, 2532, 2533, 2534, 2535, 2536, 2537, 2538, 2539, 2540, 2541, 2866, 4160, 4172, 4173, 4174, 4175, 4740, 5616, 5617, 5618, 5619, 5620, 5621, 5622, 5623, 5624, 5625, 5626, 5627, 6061, 6062};
    public static int rings[] = {15017,15220,15018,15019,19669,15020,8082, 773, 1635, 1637, 1639, 1641, 1643, 1645, 2550, 2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566, 2568, 2570, 2572, 4202, 4657, 6465, 6737, 6731, 6735, 6735, 6583, 6733};
    public static int body[] = {22490,21752,13624,13619,13614,14936,21468,14492,15423,15424,10316,10317,10318,10319,10320,10321,10322,10323,10324,10325,10436,10432,10428,10424,10420,10416,10412,10408,10404,10400,19317,19263,19257,19251,19242,19236,19230,19221,19215,19209,19200,19194,19188,19179,19173,19167,19461,19453,19445,19428,19413,19398,19380,19382,19384,10727,11020,15034,15028,15022,20163,20151,20139,20116,14601,14600,14595,17237,13870,15606,17257,14497,17259,19337,14479,13040,19785,17193,13889,13860,13872,13886,15606,15503,11001,2936,2926,15503,11001,7902,2936,2926,2916,2906,8839,10338,10348,10330,11724,11720,13884,13858,13887,13870,2520, 10551, 7605, 7601, 7597, 13651, 7362, 7364, 636, 638, 640, 642, 644, 8064, 426, 1005, 1757, 7592, 8031, 8027, 6617, 8019, 8014, 8002, 7376, 544, 7372, 7370, 577, 7974, 8850, 7965, 7961,3793, 3775, 3773, 3771, 3769, 3767, 6139, 1135, 2499, 2501, 1035, 540, 5553, 4757, 1833, 6388, 6384, 4111, 4101, 4091, 6186, 6184, 6180, 3058, 4069, 4728, 4736, 4712, 6107, 2661, 3140, 1101, 1103, 1105, 1107, 1109, 1111, 1113, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 1129, 1131, 1133, 2583, 2591, 2599, 2607, 2615, 2623, 2653, 2669, 3481, 4712, 4720, 4728, 4749, 4892, 4893, 4894, 4895, 4916, 4917, 4918, 4919, 4964, 4965, 4966, 4967, 6107, 6133, 6322, 6322, 6129, 75, 6916, 6916, 4111, 6654, 6654, 75, 7399, 7390, 7374, 5575, 2503, 6341, 6351, 3387, 5030, 5032, 5034, 5030, 5032, 5034, 7392, 546,19342};
    public static int legs[] = {21760,22486,13617,13622,13627,14490,19343,19344,14938,21469,15425,10438,10434,10430,10426,10422,10418,10414,10410,10406,10402,10394,10396,19320,19266,19269,19259,19261,19253,19255,19248,19245,19238,19240,19232,19234,19224,19227,19217,19219,19211,19213,19203,19206,19196,19198,19190,19192,19182,19185,19175,19177,19169,19171,19463,19455,19447,19431,19434,19416,19419,19401,19404,19386,19388,19390,10726,15036,15035,15030,15029,15024,15023,11022,20155,20167,20143,20117,14603,14604,16865,19786,13041,13042,16687,14734,15610,15505,13385,13863,13875,14501,13892,19338,17339,16689,13895,2918,2898,2908,2928,7903,8840,7937,10340,7923,10346,10332,11726,11722,13890,13873,13861,13893,1089, 2522, 7606, 7602, 7598, 7378, 7380, 7382, 7368, 7366, 7388, 646, 648, 650, 652, 654, 428, 1097, 1095, 7593, 8032, 8028, 6625, 8020, 8015, 7384, 11286, 7966, 6141, 1835, 538, 1033, 5555, 4759, 6386, 6390, 2497, 2495, 2493, 1099, 4113, 4103, 4093, 6924, 6187, 6185, 6181, 3059, 4510, 4070, 6108, 538, 542, 548, 1011, 1013, 1015, 1067, 1069, 1071, 1073, 1075, 1077, 1079, 1081, 1083, 1085, 1087, 1089, 1091, 1093, 2585, 2593, 2601, 2609, 2617, 2625, 2655, 2663, 2671, 3059, 3389, 3472, 3473, 3474, 3475, 3476, 3477, 3478, 3479, 3480, 3483, 3485, 3795, 4087, 4585, 4712, 4714, 4722, 4730, 4738, 4751, 4759, 4874, 4875, 4876, 4877, 4898, 4899, 4900, 4901, 4922, 4923, 4924, 4925, 4946, 4947, 4948, 4949, 4970, 4971, 4972, 4973, 4994, 4995, 4996, 4997, 5048, 5050, 5052, 5576, 6107, 6130, 6187, 6390, 6386, 6390, 6396, 6404, 6135, 6809, 6916, 4091, 4111, 6655, 6654, 7398, 7398, 7386, 6324, 6343, 6353, 3387, 5036, 5038, 5040};
    public static int platebody[] = {21752,22490,13624,13619,13614,21468,14492,14936,15423,15424,10436,10432,10428,10424,10420,10416,10412,10408,10404,10400,19317,19263,19257,19251,19242,19236,19230,19221,19215,19209,19200,19194,19188,19179,19173,19167,19428,19413,19398,19380,19384,19382,10727,11020,20163,20151,20139,20116,15503,14601,14600,14595,17237,13870,15606,17257,14497,17259,14479,13040,19785,17193,13889,13860,13872,13886,15606,15503,11001,2936,2926,2916,2906,7902,8839,10338,10348,10330,11724,11720,7880,13884,13858,13887,13870,7608, 1125, 2520, 10551, 7399, 13651, 636, 638, 640, 642, 644, 426, 8031, 8027, 6617, 8019, 8014, 8002, 544, 577, 7974, 8850, 7965, 7961, 3793, 3773, 3775, 3771, 3769, 3767, 6139, 1035, 540, 5553, 4757, 1833, 1835, 6388, 6384, 4111, 4101, 4868, 4869, 4870, 4871, 4892, 4893, 4894, 4895, 4916, 4917, 4918, 4919, 4940, 4941, 4942, 4943, 4964, 4965, 4966, 4967, 4988, 4989, 4990, 0x2f9a0eb, 6186, 6184, 6180, 3058, 4509, 18706, 4069, 4728, 4736, 4712, 6107, 2661, 3140, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 2583, 2591, 2599, 2607, 2615, 6322, 2623, 2653, 2669, 3481, 4720, 4728, 4749, 2661, 6129, 6916, 4091, 6654, 6133, 75, 7399, 7390, 5575, 6341, 6351, 3387, 5030, 5032, 5034, 7392};
    public static int fullHelm[] = {22482,21736,14494,19341,21467,20772,15422,13115,13111,13113,13107,13109,19437,19422,19407,20147,10723,10728,11021,20135,20115,7906,11718,3753, 3755, 3749, 3751,10828,9010,13866,13878,7607,7603,1167,7599,11665,4515,11664,11663,7841,13876,7603, 7607, 2679,2682,2685,2688,2691,2694,2697,2700,2703,2706,2709,2712,2715,2718,2721,2724,2727,2730,2733,2736,2739,2741,2744,3628,4708,3625,7610,2044,2046,2048,2050,2052,2054,2056,2058,2060,2062,2062,2064,2066,2068,2070,2072,2074,2076,2078,2080,2082,2084,2086,2088,2090,1169,4716,13898,19336,16709,11335,16711,11002,2900,4502, 13263, 2524, 2526, 2518, 7612, 7611, 7603, 7607, 7603, 13263, 7612, 7611, 8034, 8035, 8036, 1147, 7992, 8030, 6623, 8024, 8022, 8013, 7995, 7996, 7997, 7998, 7999, 8000, 8001, 7990, 7971, 7967, 7963, 3748, 6137, 6128, 1149, 3751, 7594, 4708, 4716, 4745, 4732, 5554, 4753, 4732, 4753, 6188, 4511, 4056, 4071, 4724, 6109, 2665, 1153, 1155, 1157, 1159, 1161, 1163, 1165, 2587, 2595, 2605, 2613, 2619, 2627, 2657, 2673, 3486, 6402, 6394, 6131, 74, 74, 7539, 7539, 7534, 5574, 6326};
    public static int fullMask[] = {20147,20159,19296,9010,13878,9119,7966,17061,19278,19275,19272,19293,19302,19305,19281,19284,19287,19290,19299,4515,7837,7838,7839,2900, 13263, 2524, 2526, 2518, 7612, 7611, 6623, 7990, 8013, 8022, 7594, 1153, 1155, 1157, 1159, 1161, 1163, 1165, 4732, 5554, 4753, 4611, 6188, 3507, 4511, 4056, 4071, 4724, 2665, 1053, 1055, 1057,4502,4503,4501};
	public static int halfHelm[] = {20949,20950,20951,14494,19341,9472,13672,21467,20952,20772,13105,19376,19377,19374,9814,9809,9812,9803,9806,9797,9800,9791,9794,9785,9788,9779,9782,9773,9776,9767,9770,9761,9764,9755,9758,9749,9752,19465,19457,19449,20147,11021,14743,14745,14747,14749,14751,14753,14755,14757,14759,14761,14763,14765,14767,14769,14771,14773,14775,14777,14779,14781,14783,14785,14787,14789,14791,3753, 3755, 3749, 3751,10828,9010,13866,13878,7607,7603,1167,7599,11665,4515,11664,11663,7841,13876,7603, 7607, 2679,2682,2685,2688,2691,2694,2697,2700,2703,2706,2709,2712,2715,2718,2721,2724,2727,2730,2733,2736,2739,2741,2744,3628,4708,3625,7610,2044,2046,2048,2050,2052,2054,2056,2058,2060,2062,2062,2064,2066,2068,2070,2072,2074,2076,2078,2080,2082,2084,2086,2088,2090,1169,4716};

	public static int Clue[] = {3488,3486,3488,3486,3488,2856,2857,2858,2856,2857,2858,2856,2857,2858,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2856,2857,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,2653,2655,2657,2659,2661,2663,2665,2667,2669,2671,2673,2675,638,640,642,648,650,652,658,660,662,2589,3481,3483,3485,7597,7598,7599,7601,7602,7603,7605, 7606,7607,4183,2639,2633,2635,2637,2641,2643,2651,2997,3478,3479,3480,6583,6617,6623,6625, 6633,6619,6629,7336,7342,7348,7354,7360,7370,7372,7374,7378,7380,7382,7384,7810,7811,7812 };

	// All other IDs are weapons (I hope)

	public static boolean[] itemStackable = new boolean[25838];
	public static boolean[] itemIsNote = new boolean[25838];
	public static boolean[] itemTradeable = new boolean[25838];
	public static boolean[] itemSellable = new boolean[25838];
	public static boolean[] itemTwoHanded = new boolean[25838];

	static {
		int counter = 0;
		int c;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					"config/stackable.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0)
					itemStackable[counter] = false;
				else
					itemStackable[counter] = true;
				counter++;
				itemStackable[11733] = true;
				itemStackable[15243] = true;
				itemStackable[6570] = false;
				itemStackable[6666] = false;
				itemStackable[6729] = false;
				itemStackable[6731] = false;
				itemStackable[6735] = false;
				itemStackable[6737] = false;
				itemStackable[6733] = false;
				itemStackable[6739] = false;
				itemStackable[11212] = true;
				itemStackable[7618] = true;
				itemStackable[7774] = true;
				itemStackable[7775] = true;
				itemStackable[7776] = true;
				itemStackable[6685] = false;
				itemStackable[6687] = false;
				itemStackable[6689] = false;
				itemStackable[6691] = false;
				itemStackable[6568] = false;
				itemStackable[9075] = true;
				itemStackable[553] = true;
				itemStackable[13879] = true;
				itemStackable[13883] = true;
				itemStackable[7858] = true;
				itemStackable[7859] = true;
				itemStackable[7860] = true;
				itemStackable[7861] = true;
				itemStackable[7862] = true;
				itemStackable[7863] = true;
				itemStackable[7864] = true;
				itemStackable[7865] = true;
				itemStackable[7866] = true;
				itemStackable[7867] = true;
				itemStackable[7868] = true;
				itemStackable[7869] = true;
				itemStackable[7870] = true;
				itemStackable[7871] = true;
				itemStackable[621] = true;
				itemStackable[9244] = true;
				itemStackable[7872] = true;
				itemStackable[7963] = true;
				itemStackable[7945] = true;
				itemStackable[7947] = true;
				itemStackable[7387] = true;
				itemStackable[7389] = true;
				itemStackable[7391] = true;
				itemStackable[9341] = true;
				itemStackable[7393] = true;
				itemStackable[7395] = true;
				itemStackable[7397] = true;	
				itemStackable[10000] = true;
				itemStackable[10001] = true;
				itemStackable[10002] = true;	
				itemStackable[6915] = true;	//Master Wands -- Jags				
				itemStackable[6917] = true;	//Inf tops -- Jags				
				itemStackable[6919] = true;	//Inf hats -- Jags				
				itemStackable[6921] = true;	//Inf boots -- Jags				
				itemStackable[6923] = true;	//Inf gloves -- Jags				
				itemStackable[6925] = true;	//Inf bottoms -- Jags	
				itemStackable[6890] = true;	//Mage's books -- Jags	
				itemStackable[7401] = true;	//Enchanted hat -- Jags	
				itemStackable[10000] = true;
				itemStackable[10001] = true;
				itemStackable[10002] = true;
				itemStackable[13735] = true;
				itemStackable[13737] = true;
				itemStackable[13739] = true;				
				itemStackable[13741] = true;	
				itemStackable[13743] = true;	
				itemStackable[13745] = true;
				itemStackable[17362] = true;
				itemStackable[16712] = true;
				itemStackable[16690] = true;
				itemStackable[16294] = true;
				itemStackable[16360] = true;
				itemStackable[19282] = true;
				itemStackable[19285] = true;
				itemStackable[19288] = true;
				itemStackable[19291] = true;
				itemStackable[19300] = true;
				itemStackable[19303] = true;
				itemStackable[19306] = true;	
				itemStackable[14485] = true;
				itemStackable[17260] = true;
				itemStackable[11727] = true;
				itemStackable[11725] = true;
				itemStackable[11719] = true;
				itemStackable[11721] = true;
				itemStackable[11723] = true;
				itemStackable[11284] = true;
				itemStackable[11695] = true;
				itemStackable[11697] = true;
				itemStackable[11699] = true;
				itemStackable[11701] = true;
				itemStackable[11731] = true;
				itemStackable[11336] = true;
				itemStackable[19297] = true;
				itemStackable[15774] = true;
				itemStackable[16404] = true;
				itemStackable[17040] = true;
				itemStackable[16888] = true;
				itemStackable[17238] = true;
				itemStackable[16756] = true;
				itemStackable[16866] = true;
				itemStackable[16932] = true;
				itemStackable[17018] = true;
				itemStackable[17172] = true;
				itemStackable[17266] = true;
				itemStackable[17268] = true;
				itemStackable[17270] = true;
				itemStackable[17272] = true;
				itemStackable[17274] = true;
				itemStackable[17280] = true;
				itemStackable[17282] = true;
				itemStackable[17284] = true;
				itemStackable[17286] = true;
				itemStackable[17288] = true;
				itemStackable[16956] = true;
				itemStackable[15273] = true;
				itemStackable[20136] = true;
				itemStackable[20140] = true;
				itemStackable[20144] = true;
				itemStackable[20148] = true;
				itemStackable[20152] = true;
				itemStackable[20156] = true;
				itemStackable[20160] = true;
				itemStackable[20164] = true;
				itemStackable[20168] = true;
				itemStackable[20172] = true;
				itemStackable[13888] = true;
				itemStackable[15487] = true;
				itemStackable[13653] = false;
				itemStackable[13654] = false;
				
			}
			dataIn.close();
			System.out.println("Loaded " + counter + " stackable data.");
		} catch (IOException e) {
			System.out
					.println("Critical error while loading stackabledata! Trace:");
			e.printStackTrace();
		}

		counter = 0;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					"config/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0)
					itemIsNote[counter] = true;
					
				else
					itemIsNote[counter] = false;
				counter++;
				itemIsNote[9511] = false;
				itemIsNote[11733] = true;
				itemIsNote[6915] = true; //Master wand - Jags
				itemIsNote[6917] = true; //Infinifty tops - Jags
				itemIsNote[6919] = true; //Infinity hat -Jags
				itemIsNote[6921] = true; //removed - Pat and Jags
				itemIsNote[6923] = true; //Infinity gloves - Jags
				itemIsNote[6925] = true; //Infinity bottoms - Jags
				itemIsNote[6890] = true; //removed - Pat and Jags
				itemIsNote[6572] = true;
				itemIsNote[6686] = true;
				itemIsNote[6688] = true;
				itemIsNote[6915] = true;
				itemIsNote[6917] = true;
				itemIsNote[6919] = true;
				itemIsNote[6921] = true;
				itemIsNote[6923] = true;
				itemIsNote[6925] = true;
				itemIsNote[6890] = true;
				itemIsNote[6690] = true;
				itemIsNote[6692] = true;
				itemIsNote[6732] = true;
				itemIsNote[6734] = true;
				itemIsNote[6736] = true;
				itemIsNote[6738] = true;
				itemIsNote[8500] = true;
				itemIsNote[8501] = true;
				itemIsNote[8502] = true;
				itemIsNote[8503] = true;
				itemIsNote[8504] = true;
				itemIsNote[8505] = true;
				itemIsNote[8506] = true;
				itemIsNote[8507] = true;
				itemIsNote[8508] = true;
				itemIsNote[8509] = true;
				itemIsNote[8510] = true;
				itemIsNote[8511] = true;
				itemIsNote[8512] = true;
				itemIsNote[8513] = true;
				itemIsNote[8514] = true;
				itemIsNote[8515] = true;
				itemIsNote[8516] = true;
				itemIsNote[8517] = true;
				itemIsNote[8518] = true;
				itemIsNote[8519] = true;
				itemIsNote[8520] = true;
				itemIsNote[7401] = true;
				itemIsNote[7402] = true;
				itemIsNote[7403] = true;
				itemIsNote[2526] = false;
				itemIsNote[2520] = false;
				itemIsNote[2528] = false;
				itemIsNote[2522] = false;
				itemIsNote[2524] = false;
				itemIsNote[7945] = true;
				itemIsNote[7947] = true;
				itemIsNote[6725] = true;
				itemIsNote[6726] = true;
				itemIsNote[6727] = true;
				itemIsNote[7387] = true;
				itemIsNote[7389] = true;
				itemIsNote[7391] = true;	
				itemIsNote[7393] = true;
				itemIsNote[7395] = true;
				itemIsNote[7397] = true;
				itemIsNote[10000] = true;
				itemIsNote[10001] = true;
				itemIsNote[10002] = true;
				itemIsNote[13735] = true;
				itemIsNote[13737] = true;
				itemIsNote[13739] = true;				
				itemIsNote[13741] = true;	
				itemIsNote[13743] = true;	
				itemIsNote[13745] = true;
				itemIsNote[17362] = true;
				itemIsNote[16712] = true;
				itemIsNote[16690] = true;
				itemIsNote[16294] = true;
				itemIsNote[16360] = true;
				itemIsNote[19282] = true;
				itemIsNote[19285] = true;
				itemIsNote[19288] = true;
				itemIsNote[19291] = true;
				itemIsNote[19300] = true;
				itemIsNote[19303] = true;
				itemIsNote[19306] = true;	
				itemIsNote[14485] = true;
				itemIsNote[17260] = true;
				itemIsNote[11727] = true;
				itemIsNote[11725] = true;
				itemIsNote[11719] = true;
				itemIsNote[11721] = true;
				itemIsNote[11723] = true;
				itemIsNote[11284] = true;
				itemIsNote[11695] = true;
				itemIsNote[11697] = true;
				itemIsNote[11699] = true;
				itemIsNote[11701] = true;
				itemIsNote[11731] = true;
				itemIsNote[11336] = true;
				itemIsNote[19297] = true;
				itemIsNote[15774] = true;
				itemIsNote[16404] = true;
				itemIsNote[17040] = true;
				itemIsNote[16888] = true;
				itemIsNote[17238] = true;
				itemIsNote[16756] = true;
				itemIsNote[16866] = true;
				itemIsNote[16932] = true;
				itemIsNote[17018] = true;
				itemIsNote[17172] = true;
				itemIsNote[17266] = true;
				itemIsNote[17268] = true;
				itemIsNote[17270] = true;
				itemIsNote[17272] = true;
				itemIsNote[17274] = true;
				itemIsNote[17280] = true;
				itemIsNote[17282] = true;
				itemIsNote[17284] = true;
				itemIsNote[17286] = true;
				itemIsNote[17288] = true;
				itemIsNote[16956] = true;
				itemIsNote[15273] = true;
				itemIsNote[20136] = true;
				itemIsNote[20140] = true;
				itemIsNote[20144] = true;
				itemIsNote[20148] = true;
				itemIsNote[20152] = true;
				itemIsNote[20156] = true;
				itemIsNote[20160] = true;
				itemIsNote[20164] = true;
				itemIsNote[20168] = true;
				itemIsNote[20172] = true;
				itemIsNote[13888] = true;
				itemIsNote[15487] = true;
				itemIsNote[13654] = false;
				
			}
			dataIn.close();
			System.out.println("Loaded " + counter + " note data.");
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}

		counter = 0;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					"config/sellable.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0)
					itemSellable[counter] = true;
				else
					itemSellable[counter] = false;
				counter++;
			itemSellable[7774] = false;
				itemSellable[7776] = false;
				itemSellable[7775] = true;
				itemSellable[6529] = false;
				itemSellable[4828] = false;
				itemSellable[4827] = false;
				
				itemSellable[6585] = false;
				itemSellable[6586] = false;
				itemSellable[1464] = false;
				itemSellable[384] = false;
				itemSellable[386] = false;
				itemSellable[21406] = false;
				itemSellable[6731] = false;
				itemSellable[6732] = false;
				itemSellable[6733] = false;
				itemSellable[6734] = false;
				itemSellable[6735] = false;
				itemSellable[6736] = false;
				itemSellable[6737] = false;
				itemSellable[6920] = false;
				itemSellable[11732] = false;
				itemSellable[6570] = false;
				itemSellable[882] = false;
				itemSellable[884] = false;
				itemSellable[886] = false;
				itemSellable[888] = false;
				itemSellable[890] = false;
				itemSellable[892] = false;
				itemSellable[11212] = false;
				itemSellable[537] = false;
				itemSellable[536] = false;
				
			}
			dataIn.close();
			System.out.println("Loaded " + counter + " sellable data.");
		} catch (IOException e) {
			System.out
					.println("Critical error while loading sellabledata! Trace:");
			e.printStackTrace();
		}

		counter = 0;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					"config/tradeable.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0)
					itemTradeable[counter] = true;
				else
					itemTradeable[counter] = false;
					itemTradeable[7776] = false;
					itemTradeable[19785] = false;
					itemTradeable[19786] = false;
					itemTradeable[10551] = false;
					itemTradeable[18351] = true;
					itemTradeable[18353] = true;
					itemTradeable[7959] = true;
					itemTradeable[1854] = false;
					itemTradeable[8844] = false;
					itemTradeable[7454] = false;
					itemTradeable[7455] = false;
					itemTradeable[7456] = false;
					itemTradeable[7457] = false;
					itemTradeable[7458] = false;
					itemTradeable[7459] = false;
					itemTradeable[7460] = false;
					itemTradeable[7461] = false;
					itemTradeable[21406] = false;
					itemTradeable[7462] = false;
					itemTradeable[7890] = false;
					itemTradeable[7979] = false;		
					itemTradeable[7774] = false;						
				counter++;

			}
			dataIn.close();
			System.out.println("Loaded " + counter + " tradeable data.");
		} catch (IOException e) {
			System.out
					.println("Critical error while loading tradeabledata! Trace:");
			e.printStackTrace();
		}

		counter = 0;

		try {
			FileInputStream dataIn = new FileInputStream(new File(
					"config/twohanded.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0)
					itemTwoHanded[counter] = true;
				else
					itemTwoHanded[counter] = false;
					itemTwoHanded[10887] = true;
					itemTwoHanded[15242] = true;
					itemTwoHanded[16426] = true;
					itemTwoHanded[16424] = true;
					itemTwoHanded[18370] = true;
					itemTwoHanded[15242] = true;
					itemTwoHanded[16908] = true;
					itemTwoHanded[16426] = true;
					itemTwoHanded[18370] = true;
					itemTwoHanded[11715] = true;
					itemTwoHanded[11716] = true;
					itemTwoHanded[11717] = true;
					itemTwoHanded[11714] = true;
					itemTwoHanded[16424] = true;
					itemTwoHanded[16910] = true;
			itemTwoHanded[18353] = true;
			itemTwoHanded[13390] = true;
			itemTwoHanded[8842] = true;
			itemTwoHanded[13907] = true;
			itemTwoHanded[20171] = true;
				counter++;
			}
			
			dataIn.close();
			System.out.println("Loaded " + counter + " twohanded data.");
		} catch (IOException e) {
			System.out
					.println("Critical error while loading twohandeddata! Trace:");
			e.printStackTrace();
		}

	}

	public static int randomCape() {
		return capes[(int) (Math.random() * capes.length)];
	}

	public static int randomBoots() {
		return boots[(int) (Math.random() * boots.length)];
	}

	public static int randomGloves() {
		return gloves[(int) (Math.random() * gloves.length)];
	}

	public static int randomShield() {
		return shields[(int) (Math.random() * shields.length)];
	}

	public static int randomHat() {
		return hats[(int) (Math.random() * hats.length)];
	}

	public static int randomAmulet() {
		return amulets[(int) (Math.random() * amulets.length)];
	}

	public static int randomArrows() {
		return arrows[(int) (Math.random() * arrows.length)];
	}

	public static int randomRing() {
		return rings[(int) (Math.random() * rings.length)];
	}

	public static int randomBody() {
		return body[(int) (Math.random() * body.length)];
	}

	public static int randomLegs() {
		return legs[(int) (Math.random() * legs.length)];
	}
	/* Fullbody is an item that covers your arms. */
	private static final String[] FULL_BODY = {"Morrigan's leather body", "hauberk","Ghostly robe","Monk's robe","Granite","Vesta", "Runecrafter robe", "top","shirt","platebody","Ahrims robetop","Karils leathertop","brassard","Robe top","robetop","platebody (t)","platebody (g)","chestplate","torso", "Dragon chainbody"};

	public static boolean isPlate(int itemID) {
		/*String itemName = Server.getItemManager().getItemDefinition(itemID).getName();
		for (int i = 0; i < FULL_BODY.length; i++) {
			if (itemName.contains(FULL_BODY[i])) {
				return true;
			}
		}	*/	
		for (int i = 0; i < platebody.length; i++){
			if (platebody[i] == itemID)
				return true;
		}
		return false;
	}

	public static boolean isFullHelm(int itemID) {
		for (int i = 0; i < fullHelm.length; i++)
			if (fullHelm[i] == itemID)
				return true;
		return false;
	}
   public static boolean isHalfHelm(int itemID) {
      for (int element : halfHelm)
         if (element == itemID)
            return true;
      return false;
   }
	public static boolean isFullMask(int itemID) {
		for (int i = 0; i < fullMask.length; i++)
			if (fullMask[i] == itemID)
				return true;
		return false;
	}
	public static int smithing_frame[][][] = {
			{ { 1205, 1, 1, 1, 1125, 1094 }, { 1351, 1, 1, 1, 1126, 1091 },
					{ 1422, 1, 2, 1, 1129, 1093 },
					{ 1139, 1, 3, 1, 1127, 1102 },
					{ 1277, 1, 3, 1, 1128, 1085 },
					{ 819, 10, 4, 1, 1124, 1107 },
					{ 4819, 15, 4, 1, 13357, 13358 },
					{ 39, 15, 5, 1, 1130, 1108 },
					{ 1321, 1, 5, 2, 1116, 1087 },
					{ 1291, 1, 6, 2, 1089, 1086 },
					{ 1155, 1, 7, 2, 1113, 1103 },
					{ 864, 5, 7, 1, 1131, 1106 },
					{ 1173, 1, 8, 2, 1114, 1104 },
					{ 1337, 1, 9, 3, 1118, 1083 },
					{ 1375, 1, 10, 3, 1095, 1092 },
					{ 1103, 1, 11, 3, 1109, 1098 },
					{ 1189, 1, 12, 3, 1115, 1105 },
					{ 3095, 1, 13, 2, 8428, 8429 },
					{ 1307, 1, 14, 3, 1090, 1088 },
					{ 1087, 1, 16, 3, 1111, 1100 },
					{ 1075, 1, 16, 3, 1110, 1099 },
					{ 1117, 1, 18, 5, 1112, 1101 },/* Specials */
					{ 1794, 1, 4, 1, 1132, 1096 } },
			{ { 1203, 1, 15, 1, 1125, 1094 }, { 1349, 1, 16, 1, 1126, 1091 },
					{ 1420, 1, 17, 1, 1129, 1093 },
					{ 1137, 1, 18, 1, 1127, 1102 },
					{ 1279, 1, 19, 1, 1128, 1085 },
					{ 820, 10, 19, 1, 1124, 1107 },
					{ 4820, 15, 19, 1, 13357, 13358 },
					{ 40, 15, 20, 1, 1130, 1108 },
					{ 1323, 1, 20, 2, 1116, 1087 },
					{ 1293, 1, 21, 2, 1089, 1086 },
					{ 1153, 1, 22, 2, 1113, 1103 },
					{ 863, 5, 22, 1, 1131, 1106 },
					{ 1175, 1, 23, 2, 1114, 1104 },
					{ 1335, 1, 24, 3, 1118, 1083 },
					{ 1363, 1, 25, 3, 1095, 1092 },
					{ 1101, 1, 26, 3, 1109, 1098 },
					{ 1191, 1, 27, 3, 1115, 1105 },
					{ 3096, 1, 28, 2, 8428, 8429 },
					{ 1309, 1, 29, 3, 1090, 1088 },
					{ 1081, 1, 31, 3, 1111, 1100 },
					{ 1067, 1, 31, 3, 1110, 1099 },
					{ 1115, 1, 33, 5, 1112, 1101 },/* Specials */
					{ 4540, 1, 26, 1, 11459, 11461 } },
			{ { 1207, 1, 30, 1, 1125, 1094 }, { 1353, 1, 31, 1, 1126, 1091 },
					{ 1424, 1, 32, 1, 1129, 1093 },
					{ 1141, 1, 33, 1, 1127, 1102 },
					{ 1281, 1, 34, 1, 1128, 1085 },
					{ 821, 10, 34, 1, 1124, 1107 },
					{ 1539, 15, 34, 1, 13357, 13358 },
					{ 41, 15, 35, 1, 1130, 1108 },
					{ 1325, 1, 35, 2, 1116, 1087 },
					{ 1295, 1, 36, 2, 1089, 1086 },
					{ 1157, 1, 37, 2, 1113, 1103 },
					{ 865, 5, 37, 1, 1131, 1106 },
					{ 1177, 1, 38, 2, 1114, 1104 },
					{ 1339, 1, 39, 3, 1118, 1083 },
					{ 1365, 1, 40, 3, 1095, 1092 },
					{ 1105, 1, 41, 3, 1109, 1098 },
					{ 1193, 1, 42, 3, 1115, 1105 },
					{ 3097, 1, 43, 2, 8428, 8429 },
					{ 1311, 1, 44, 3, 1090, 1088 },
					{ 1083, 1, 46, 3, 1111, 1100 },
					{ 1069, 1, 46, 3, 1110, 1099 },
					{ 1119, 1, 48, 5, 1112, 1101 },/* Specials */
					{ 4544, 1, 49, 1, 11459, 11461 },
					{ 2370, 1, 36, 1, 1135, 1134 } },
			{ { 1209, 1, 50, 1, 1125, 1094 }, { 1355, 1, 51, 1, 1126, 1091 },
					{ 1428, 1, 52, 1, 1129, 1093 },
					{ 1143, 1, 53, 1, 1127, 1102 },
					{ 1285, 1, 53, 1, 1128, 1085 },
					{ 822, 10, 54, 1, 1124, 1107 },
					{ 4822, 15, 54, 1, 13357, 13358 },
					{ 42, 15, 55, 1, 1130, 1108 },
					{ 1329, 1, 55, 2, 1116, 1087 },
					{ 1299, 1, 56, 2, 1089, 1086 },
					{ 1159, 1, 57, 2, 1113, 1103 },
					{ 866, 5, 57, 1, 1131, 1106 },
					{ 1181, 1, 58, 2, 1114, 1104 },
					{ 1343, 1, 59, 3, 1118, 1083 },
					{ 1369, 1, 60, 3, 1095, 1092 },
					{ 1109, 1, 61, 3, 1109, 1098 },
					{ 1197, 1, 62, 3, 1115, 1105 },
					{ 3099, 1, 63, 2, 8428, 8429 },
					{ 1315, 1, 64, 3, 1090, 1088 },
					{ 1085, 1, 66, 3, 1111, 1100 },
					{ 1071, 1, 66, 3, 1110, 1099 },
					{ 1121, 1, 68, 5, 1112, 1101 } },
			{ { 1211, 1, 70, 1, 1125, 1094 }, { 1357, 1, 71, 1, 1126, 1091 },
					{ 1430, 1, 72, 1, 1129, 1093 },
					{ 1145, 1, 73, 1, 1127, 1102 },
					{ 1287, 1, 74, 1, 1128, 1085 },
					{ 823, 10, 74, 1, 1124, 1107 },
					{ 4823, 15, 74, 1, 13357, 13358 },
					{ 43, 15, 75, 1, 1130, 1108 },
					{ 1331, 1, 75, 2, 1116, 1087 },
					{ 1301, 1, 76, 2, 1089, 1086 },
					{ 1161, 1, 77, 2, 1113, 1103 },
					{ 867, 5, 77, 1, 1131, 1106 },
					{ 1183, 1, 78, 2, 1114, 1104 },
					{ 1345, 1, 79, 3, 1118, 1083 },
					{ 1371, 1, 80, 3, 1095, 1092 },
					{ 1111, 1, 81, 3, 1109, 1098 },
					{ 1199, 1, 82, 3, 1115, 1105 },
					{ 3100, 1, 83, 2, 8428, 8429 },
					{ 1317, 1, 84, 3, 1090, 1088 },
					{ 1091, 1, 86, 3, 1111, 1100 },
					{ 1073, 1, 86, 3, 1110, 1099 },
					{ 1123, 1, 88, 5, 1112, 1101 } },
			{ { 1213, 1, 85, 1, 1125, 1094 }, { 1359, 1, 86, 1, 1126, 1091 },
					{ 1432, 1, 87, 1, 1129, 1093 },
					{ 1147, 1, 88, 1, 1127, 1102 },
					{ 1289, 1, 89, 1, 1128, 1085 },
					{ 824, 10, 89, 1, 1124, 1107 },
					{ 4824, 15, 89, 1, 13357, 13358 },
					{ 44, 15, 90, 1, 1130, 1108 },
					{ 1333, 1, 90, 2, 1116, 1087 },
					{ 1303, 1, 91, 2, 1089, 1086 },
					{ 1163, 1, 92, 2, 1113, 1103 },
					{ 868, 5, 92, 1, 1131, 1106 },
					{ 1185, 1, 93, 2, 1114, 1104 },
					{ 1347, 1, 94, 3, 1118, 1083 },
					{ 1373, 1, 95, 3, 1095, 1092 },
					{ 1113, 1, 96, 3, 1109, 1098 },
					{ 1201, 1, 97, 3, 1115, 1105 },
					{ 3101, 1, 98, 2, 8428, 8429 },
					{ 1319, 1, 99, 3, 1090, 1088 },
					{ 1093, 1, 99, 3, 1111, 1100 },
					{ 1079, 1, 99, 3, 1110, 1099 },
					{ 1127, 1, 99, 5, 1112, 1101 } }
	// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
	// dagger axe mace medium sword dart tips nails arrow heads scimitar long sword
	// full helmet knives square warhammer battle axe chain kite claws 2-handed
	// skirt legs body lantern/wire studs
	};
	public static int SmithingItems[][] = new int[5][5];

}
