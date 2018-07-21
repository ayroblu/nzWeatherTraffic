package benl.student.datacol;

import java.util.HashMap;
import java.util.Map;

import android.util.SparseArray;

public class TrafficCams {
	// Assumes url: http://www.nzta.govt.nz/traffic/current-conditions/webcams/webcam-images/\w+.jpg
	// Assume Auckland url: http://www.trafficnz.info/camera/\w+.jpg"
	private static final String[] AUCKLAND_SH = {
		"SH1 Northern",
		"SH1 Southern", 
		"SH16 North Western", 
		"SH18 Upper Harbour", 
		"SH20 South Western", 
		"SH20A", 
		"SH20B", 
	};
	private static final String[] NZ_CITIES = {
		"Hamilton",
		"Central North Island", 
		"Wellington", 
		"Tauranga", 
		"Dunedin", 
		"Christchurch",
	};
	
	// -----Central
	private static final String[] CENTRAL_CAM_NAME = {
		"Waiouru",
	};
	private static final String[] CENTRAL_CAM_URL = {
		"waiouru",
	};
	
	// ------Christchurch
	private static final String[] CHRISTCHURCH_CAM_NAME = {
		"Anzac Pages",
		"Belfast",
		"Blenheim Curletts",
		"Brougham",
		"Main Sth Carmen",
		"Curletts Yaldhurst",
		"Main Nth QEII",
		"Yaldhurst Russley",
		"Memorial Russley",
		"Main North Road",
		"Johns West",
		"Dyers Linwood",
		"SH1 Waimakariri Bridge",

	};
	private static final String[] CHRISTCHURCH_CAM_URL = {
		"anzac",
		"belfast",
		"blenheim",
		"brougham",
		"carmen",
		"curletts",
		"mainqe2",
		"masham",
		"memorial",
		"johnsnth",
		"johnswest",
		"linwood",
		"waimakbridge",
	};
	
	// ------Dunedin
	private static final String[] DUNEDIN_CAM_NAME = {
		"Dunedin 1",
		"SH1 Motorway at Andersons Bay road",
		"Caversham Highway Improvements - Mornington Rd",
	};
	private static final String[] DUNEDIN_CAM_URL = {
		"dunedin1",
		"dunedin2",
		"snap_c1",
	};

	// ------Hamilton
	private static final String[] HAMILTON_CAM_NAME = {
		"SH1 Cambridge Rd SH26 Morrinsville Rd North",
		"SH1 Cambridge Rd SH26 Morrinsville Rd South",
		"SH1 Cambridge Rd SH26 Morrinsville Rd East ",
		"SH1 Cobham Dr SH1 Cambridge Rd North",
		"SH1 Cobham Dr SH1 Cambridge Rd West",
		"SH1 Cobham Dr SH1 Cambridge Rd South",
		"Lorne St, Ohaupo Rd, Kahikatea Drive North ",
		"Lorne St, Ohaupo Rd, Kahikatea Drive West",
		"Lorne St, Ohaupo Rd, Kahikatea Drive South ",
		"Lorne St, Ohaupo Rd, Kahikatea Drive East",
		"Massey St, Lincoln St, Greenwood Rd North",
		"Massey St, Lincoln St, Greenwood Rd West",
		"Massey St, Lincoln St, Greenwood Rd South",
		"Massey St, Lincoln St, Greenwood Rd East",
		"Te Rapa Rd, Avalon Dr, Wairere Rd North",
		"Te Rapa Rd, Avalon Dr, Wairere Rd West",
		"Te Rapa Rd, Avalon Dr, Wairere Rd South",
		"Te Rapa Rd, Avalon Dr, Wairere Rd East",
	};
	private static final String[] HAMILTON_CAM_URL = {
		"wweb01_north",
		"wweb01_south",
		"wweb01_east",
		"wweb02_north",
		"wweb02_west",
		"wweb02_south",
		"wweb03_north",
		"wweb03_west",
		"wweb03_south",
		"wweb03_east",
		"wweb04_north",
		"wweb04_west",
		"wweb04_south",
		"wweb04_east",
		"wweb05_north",
		"wweb05_west",
		"wweb05_south",
		"wweb05_east",
	};
	
	// ------Tauranga
	private static final String[] TAURANGA_CAM_NAME = {
		"Bayfair Roundabout towards Mt Maunganui",
		"Bayfair Roundabout towards Te Puke",
		"Bayfair Roundabout towards Girven Road",
		"Bayfair Roundabout towards Owens Place",
		"Hewletts Road towards Harbour Bridge",
		"Hewletts Road towards Harbour Bridge (zoom)",
		"Hewletts Road towards Flyover",
		"Totara Street Intersection towards Flyover",
		"Totara Street Intersection towards Tauranga",
		"Totara Street Intersection towards Totara Street",
		"Dive Crescent / Mirrielees Road",
		"Harbour Bridge towards Mt Maunganui",
		"Harbour Bridge towards Dive Crescent",
		"Chapel Street towards Harbour Bridge",
		"Chapel Street towards Otumoetai",
		"Flyover Offramp",
		"Chapel Street towards Cameron Road",
		"15th Avenue towards Fraser Street",
		"15th Avenue towards SH2",
		"15th Avenue towards Tauranga",
		"15th Avenue towards Hospital",
		"Welcome Bay Roundabout towards SH29 / Oropi",
		"Welcome bay / Ohauiti Intersection",
		"Welcome Bay R/About - Hairini St",
		"Welcome Bay R/About - to Maungatapu",
	};
	private static final String[] TAURANGA_CAM_URL = {
		"gNSKs",
		"nj77R",
		"B2ZEy",
		"u%7BpbL",
		"gKTgw",
		"z9%7BlV",
		"0uZYR",
		"dKYqm",
		"FSEOf",
		"VREzk",
		"BXhiD",
		"oTgVG",
		"oDJi7",
		"Tjbzd",
		"fmJZw",
		"diwHn",
		"yNOzr",
		"tvk36",
		"o%7B3WE",
		"5pXBU",
		"XgGcj",
		"JIqTT",
		"0IUUj",
		"Tu%5BMx",
		"v3YiX",
	};
	
	// ------Wellington
	private static final String[] WELLINGTON_CAM_NAME = {
		"Johnsonville South",
		"Ngauranga Camera 1",
		"Ngauranga Camera 2",
		"Ngauranga SH1 and SH2 Interchange",
		"Coast Road",
		"Otaki",
		"Paremata",
		"Tinakori",
		"Petone",
		"Melling",
		"Haywards",
		"Tawa",
		"Plimmerton roundabout",
		"Paekakariki",
	};
	private static final String[] WELLINGTON_CAM_URL = {
		"hel1",
		"hel2",
		"hel3",
		"sh1sh2merge",
		"cst1",
		"Otaki",
		"paremata",
		"tinakori",
		"petone",
		"melling",
		"Haywards",
		"tawa",
		"plimroundabout",
		"paekakariki",
	};

	//=======================================Auckland
	// ------SH1-Northern
	private static final String[] NORTHERN_CAM_NAME = {
		"SH1 Alpurt Web cam",
		"SH1 1 Greville Rd",
		"SH1 2 Tristram Ave",
		"SH1 3 Northcote Rd",
		"SH1 4 Esmonde Rd",
		"SH1 5 Onewa Rd",
	};
	private static final String[] NORTHERN_CAM_URL = {
		"215",
		"10",
		"20",
		"30",
		"40",
		"50",
	};

	// ------SH1-Southern
	private static final String[] SOUTHERN_CAM_NAME = {
		"SH1 1 CMJ",
		"SH1 2 Market Rd",
		"SH1 3 Greenlane Rd",
		"SH1 4 SE Highway",
		"SH1 5 Redoubt Rd",
		"SH1 6 Bairds Rd",
		"SH1 7 Alfriston Rd",
		"SH1 8 Takanini",
		"SH1 9 Walter Streven",
		"SH1 10 Pahurehure",
		"SH1 11 Rushgreen Ave",
		"SH1 12 Park Estate",
		"SH1 13 Slippery Crk",
		"SH1 17 Goodwood",
		"SH1 18 Rainbows End",
	};
	private static final String[] SOUTHERN_CAM_URL = {
		"60",
		"70",
		"80",
		"90",
		"100",
		"110",
		"120",
		"121",
		"122",
		"123",
		"124",
		"125",
		"126",
		"212",
		"214",
	};

	// ------SH16-North-Western
	private static final String[] NORTH_WESTERN_CAM_NAME = {
		"SH16 1 Bond St",
		"SH16 2 Gt North Rd",
		"SH16 3 Te Atatu Rd",
		"SH16 4 Lincoln Rd E",
		"SH16 5 Lincoln Rd W",
		"SH16 6 Hobsonville W",
		"SH16 7 Hobsonville E",
	};
	private static final String[] NORTH_WESTERN_CAM_URL = {
		"130",
		"140",
		"150",
		"160",
		"170",
		"270",
		"271",
	};

	// ------SH18-Upper-Harbour
	private static final String[] UPPER_HARBOUR_CAM_NAME = {
		"SH18 6 Tauhinu BRG",
		"SH18 7 Albany HWY",
		"SH18 8 Tauhinu East",
		"SH18 9 Paul Matthews",
		"SH18 10 Greenhithe ",
		"SH18 11 George Deane",
		"SH18 12 Upper Hbr CW",
		"SH18 13 Wicklam Lane",
		"SH18 Trig Rd",
		"SH18 Waiahora Creek",
		"SH18 Brigham Creek",
		"SH18 Sinton Rd",
		"SH18 Squadron Dr",
	};
	private static final String[] UPPER_HARBOUR_CAM_URL = {
		"171",
		"172",
		"173",
		"174",
		"175",
		"176",
		"177",
		"178",
		"221",
		"222",
		"223",
		"224",
		"225",
	};

	// ------SH20-South-Western
	private static final String[] SOUTH_WESTERN_CAM_NAME = {
		"SH20 1 Queenstown Rd",
		"SH20 2 Hillsborough ",
		"SH20 3 Melrose",
		"SH20 4 Dominion Rd",
		"SH20 5 Sandringham",
		"SH20 6 Gloucester Pk",
		"SH20 7 Onehunga ",
		"SH20 8 Rimu Rd",
		"SH20 9 Crawford Ave",
		"SH20 Coronation Rd",
		"SH20 11 Puhinui Rd",
		"SH20 12 Nesdale Road",
		"SH20 13 Plunket Ave",
		"SH20 14 Lambie Drive",
		"SH20 15 Barrowcliffe",
		"SH20 16 Great South",
	};
	private static final String[] SOUTH_WESTERN_CAM_URL = {
		"190",
		"191",
		"192",
		"193",
		"194",
		"201",
		"202",
		"203",
		"204",
		"205",
		"206",
		"207",
		"208",
		"209",
		"210",
		"211",
	};

	// ------SH20A
	private static final String[] SH20A_CAM_NAME = {
		"SH20A Montgomerie Rd",
		"SH20A Kirkbride Road",
		"SH20A Bader Drive",
	};
	private static final String[] SH20A_CAM_URL = {
		"274",
		"275",
		"276",
	};

	// ------SH20B
	private static final String[] SH20B_CAM_NAME = {
		"SH20B Prices Road",
		"SH20B Waokauri Creek",
	};
	private static final String[] SH20B_CAM_URL = {
		"272",
		"273",
	};

	private static final Map<String,String[]> AUCKLAND_NAME_MAP = new HashMap<String,String[]>() {
		private static final long serialVersionUID = 7358270473458116645L;{
		put(AUCKLAND_SH[0], NORTHERN_CAM_NAME);
		put(AUCKLAND_SH[1], SOUTHERN_CAM_NAME);
		put(AUCKLAND_SH[2], NORTH_WESTERN_CAM_NAME);
		put(AUCKLAND_SH[3], UPPER_HARBOUR_CAM_NAME);
		put(AUCKLAND_SH[4], SOUTH_WESTERN_CAM_NAME);
		put(AUCKLAND_SH[5], SH20A_CAM_NAME);
		put(AUCKLAND_SH[6], SH20B_CAM_NAME);
	}};
	private static final Map<String,String[]> AUCKLAND_URL_MAP = new HashMap<String,String[]>() {
		private static final long serialVersionUID = -2828067840652182718L;{
		put(AUCKLAND_SH[0], NORTHERN_CAM_URL);
		put(AUCKLAND_SH[1], SOUTHERN_CAM_URL);
		put(AUCKLAND_SH[2], NORTH_WESTERN_CAM_URL);
		put(AUCKLAND_SH[3], UPPER_HARBOUR_CAM_URL);
		put(AUCKLAND_SH[4], SOUTH_WESTERN_CAM_URL);
		put(AUCKLAND_SH[5], SH20A_CAM_URL);
		put(AUCKLAND_SH[6], SH20B_CAM_URL);
	}};
	private static final Map<String,String[]> NZ_CITIES_NAME_MAP = new 
			HashMap<String,String[]>() {
				private static final long serialVersionUID = -3025466007204110167L;
			{
		put(NZ_CITIES[0], HAMILTON_CAM_NAME);
		put(NZ_CITIES[1], CENTRAL_CAM_NAME);
		put(NZ_CITIES[2], WELLINGTON_CAM_NAME);
		put(NZ_CITIES[3], TAURANGA_CAM_NAME);
		put(NZ_CITIES[4], DUNEDIN_CAM_NAME);
		put(NZ_CITIES[5], CHRISTCHURCH_CAM_NAME);
	}};
	private static final Map<String,String[]> NZ_CITIES_URL_MAP = new
			HashMap<String,String[]>() {
				private static final long serialVersionUID = -2073712332380338349L;
			{
		put(NZ_CITIES[0], HAMILTON_CAM_URL);
		put(NZ_CITIES[1], CENTRAL_CAM_URL);
		put(NZ_CITIES[2], WELLINGTON_CAM_URL);
		put(NZ_CITIES[3], TAURANGA_CAM_URL);
		put(NZ_CITIES[4], DUNEDIN_CAM_URL);
		put(NZ_CITIES[5], CHRISTCHURCH_CAM_URL);
	}};
	public static final SparseArray<Map<String,String[]>> NAME_MAP = new SparseArray<Map<String,String[]>>() {{
		put(0, AUCKLAND_NAME_MAP);
		put(1, NZ_CITIES_NAME_MAP);
	}};
	public static final SparseArray<Map<String,String[]>> URL_MAP = new SparseArray<Map<String,String[]>>() {{
		put(0, AUCKLAND_URL_MAP);
		put(1, NZ_CITIES_URL_MAP);
	}};
}