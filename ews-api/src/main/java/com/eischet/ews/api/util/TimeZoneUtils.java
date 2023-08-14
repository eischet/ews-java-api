/*
 * The MIT License
 * Copyright (c) 2012 Microsoft Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eischet.ews.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Miscellany timezone functions
 */
public final class TimeZoneUtils {

    // A map of olson name > Microsoft Name.
    private static final Map<String, String> olsonTimeZoneToMs = createOlsonTimeZoneToMsMap();


    private TimeZoneUtils() {
        throw new UnsupportedOperationException();
    }


    /**
     * Convert Olson TimeZone to Microsoft TimeZone Generated using Unicode CLDR project Example:
     * https://gist.github.com/scottmac/655675e9b4d4913c539c
     *
     * @param timeZone java timezone (Olson)
     * @return a microsoft timezone identifier (ala Eastern Standard Time)
     */
    public static String getMicrosoftTimeZoneName(final TimeZone timeZone) {
        if (timeZone == null) {
            throw new IllegalArgumentException("Parameter \"timeZone\" must be defined");
        }
        final String id = timeZone.getID();
        return olsonTimeZoneToMs.get(id);
    }

    // TODO: Still Missing Europe/Kirov, America/Nuuk, Europe/Ulyanovsk,
    public static Map<String, String> createOlsonTimeZoneToMsMap() {
        final Map<String, String> map = new HashMap<String, String>();

        // --- new timezones appeared in Java 9+, added in manually from tzutil /l on a win10 machine:
        map.put("Europe/Saratov", "Saratov Standard Time");
        map.put("Europe/Astrakhan", "Astrakhan Standard Time");
        map.put("America/Punta_Arenas", "Magallanes Standard Time");

        map.put("Europe/Kirov", "W. Europe Standard Time"); // TODO: this is just a guess. I have no idea what this should actually be.
        map.put("Europe/Ulyanovsk", "W. Europe Standard Time"); // TODO: this is just a guess. I have no idea what this should actually be.
        map.put("America/Nuuk", "Alaskan Standard Time");  // TODO: this is just a wild guess. I have no idea what this should actually be.
        map.put("Europe/Kyiv", "FLE Standard Time"); // educated guess?
        map.put("America/Ciudad_Juarez", "Mountain Standard Time (Mexico)"); // guessed via wikipedia which mentions MST

        // ---- old ones:

        map.put("Africa/Abidjan", "Greenwich Standard Time");
        map.put("Africa/Accra", "Greenwich Standard Time");
        map.put("Africa/Addis_Ababa", "E. Africa Standard Time");
        map.put("Africa/Algiers", "W. Central Africa Standard Time");
        map.put("Africa/Asmara", "E. Africa Standard Time");
        map.put("Africa/Asmera", "E. Africa Standard Time");
        map.put("Africa/Bamako", "Greenwich Standard Time");
        map.put("Africa/Bangui", "W. Central Africa Standard Time");
        map.put("Africa/Banjul", "Greenwich Standard Time");
        map.put("Africa/Bissau", "Greenwich Standard Time");
        map.put("Africa/Blantyre", "South Africa Standard Time");
        map.put("Africa/Brazzaville", "W. Central Africa Standard Time");
        map.put("Africa/Bujumbura", "South Africa Standard Time");
        map.put("Africa/Cairo", "Egypt Standard Time");
        map.put("Africa/Casablanca", "Morocco Standard Time");
        map.put("Africa/Ceuta", "Romance Standard Time");
        map.put("Africa/Conakry", "Greenwich Standard Time");
        map.put("Africa/Dakar", "Greenwich Standard Time");
        map.put("Africa/Dar_es_Salaam", "E. Africa Standard Time");
        map.put("Africa/Djibouti", "E. Africa Standard Time");
        map.put("Africa/Douala", "W. Central Africa Standard Time");
        map.put("Africa/El_Aaiun", "Morocco Standard Time");
        map.put("Africa/Freetown", "Greenwich Standard Time");
        map.put("Africa/Gaborone", "South Africa Standard Time");
        map.put("Africa/Harare", "South Africa Standard Time");
        map.put("Africa/Johannesburg", "South Africa Standard Time");
        map.put("Africa/Juba", "E. Africa Standard Time");
        map.put("Africa/Kampala", "E. Africa Standard Time");
        map.put("Africa/Khartoum", "E. Africa Standard Time");
        map.put("Africa/Kigali", "South Africa Standard Time");
        map.put("Africa/Kinshasa", "W. Central Africa Standard Time");
        map.put("Africa/Lagos", "W. Central Africa Standard Time");
        map.put("Africa/Libreville", "W. Central Africa Standard Time");
        map.put("Africa/Lome", "Greenwich Standard Time");
        map.put("Africa/Luanda", "W. Central Africa Standard Time");
        map.put("Africa/Lubumbashi", "South Africa Standard Time");
        map.put("Africa/Lusaka", "South Africa Standard Time");
        map.put("Africa/Malabo", "W. Central Africa Standard Time");
        map.put("Africa/Maputo", "South Africa Standard Time");
        map.put("Africa/Maseru", "South Africa Standard Time");
        map.put("Africa/Mbabane", "South Africa Standard Time");
        map.put("Africa/Mogadishu", "E. Africa Standard Time");
        map.put("Africa/Monrovia", "Greenwich Standard Time");
        map.put("Africa/Nairobi", "E. Africa Standard Time");
        map.put("Africa/Ndjamena", "W. Central Africa Standard Time");
        map.put("Africa/Niamey", "W. Central Africa Standard Time");
        map.put("Africa/Nouakchott", "Greenwich Standard Time");
        map.put("Africa/Ouagadougou", "Greenwich Standard Time");
        map.put("Africa/Porto-Novo", "W. Central Africa Standard Time");
        map.put("Africa/Sao_Tome", "Greenwich Standard Time");
        map.put("Africa/Timbuktu", "Greenwich Standard Time");
        map.put("Africa/Tripoli", "Libya Standard Time");
        map.put("Africa/Tunis", "W. Central Africa Standard Time");
        map.put("Africa/Windhoek", "Namibia Standard Time");
        map.put("America/Anchorage", "Alaskan Standard Time");
        map.put("America/Anguilla", "SA Western Standard Time");
        map.put("America/Antigua", "SA Western Standard Time");
        map.put("America/Araguaina", "SA Eastern Standard Time");
        map.put("America/Argentina/Buenos_Aires", "Argentina Standard Time");
        map.put("America/Argentina/Catamarca", "Argentina Standard Time");
        map.put("America/Argentina/ComodRivadavia", "Argentina Standard Time");
        map.put("America/Argentina/Cordoba", "Argentina Standard Time");
        map.put("America/Argentina/Jujuy", "Argentina Standard Time");
        map.put("America/Argentina/La_Rioja", "Argentina Standard Time");
        map.put("America/Argentina/Mendoza", "Argentina Standard Time");
        map.put("America/Argentina/Rio_Gallegos", "Argentina Standard Time");
        map.put("America/Argentina/Salta", "Argentina Standard Time");
        map.put("America/Argentina/San_Juan", "Argentina Standard Time");
        map.put("America/Argentina/San_Luis", "Argentina Standard Time");
        map.put("America/Argentina/Tucuman", "Argentina Standard Time");
        map.put("America/Argentina/Ushuaia", "Argentina Standard Time");
        map.put("America/Aruba", "SA Western Standard Time");
        map.put("America/Asuncion", "Paraguay Standard Time");
        map.put("America/Atikokan", "SA Pacific Standard Time");
        map.put("America/Bahia", "Bahia Standard Time");
        map.put("America/Bahia_Banderas", "Central Standard Time (Mexico)");
        map.put("America/Barbados", "SA Western Standard Time");
        map.put("America/Belem", "SA Eastern Standard Time");
        map.put("America/Belize", "Central America Standard Time");
        map.put("America/Blanc-Sablon", "SA Western Standard Time");
        map.put("America/Boa_Vista", "SA Western Standard Time");
        map.put("America/Bogota", "SA Pacific Standard Time");
        map.put("America/Boise", "Mountain Standard Time");
        map.put("America/Buenos_Aires", "Argentina Standard Time");
        map.put("America/Cambridge_Bay", "Mountain Standard Time");
        map.put("America/Campo_Grande", "Central Brazilian Standard Time");
        map.put("America/Cancun", "Eastern Standard Time (Mexico)");
        map.put("America/Caracas", "Venezuela Standard Time");
        map.put("America/Catamarca", "Argentina Standard Time");
        map.put("America/Cayenne", "SA Eastern Standard Time");
        map.put("America/Cayman", "SA Pacific Standard Time");
        map.put("America/Chicago", "Central Standard Time");
        map.put("America/Chihuahua", "Mountain Standard Time (Mexico)");
        map.put("America/Coral_Harbour", "SA Pacific Standard Time");
        map.put("America/Cordoba", "Argentina Standard Time");
        map.put("America/Costa_Rica", "Central America Standard Time");
        map.put("America/Creston", "US Mountain Standard Time");
        map.put("America/Cuiaba", "Central Brazilian Standard Time");
        map.put("America/Curacao", "SA Western Standard Time");
        map.put("America/Danmarkshavn", "UTC");
        map.put("America/Dawson", "Pacific Standard Time");
        map.put("America/Dawson_Creek", "US Mountain Standard Time");
        map.put("America/Denver", "Mountain Standard Time");
        map.put("America/Detroit", "Eastern Standard Time");
        map.put("America/Dominica", "SA Western Standard Time");
        map.put("America/Edmonton", "Mountain Standard Time");
        map.put("America/Eirunepe", "SA Pacific Standard Time");
        map.put("America/El_Salvador", "Central America Standard Time");
        map.put("America/Ensenada", "Pacific Standard Time");
        map.put("America/Fort_Nelson", "Mountain Standard Time");
        map.put("America/Fort_Wayne", "US Eastern Standard Time");
        map.put("America/Fortaleza", "SA Eastern Standard Time");
        map.put("America/Glace_Bay", "Atlantic Standard Time");
        map.put("America/Godthab", "Greenland Standard Time");
        map.put("America/Goose_Bay", "Atlantic Standard Time");
        map.put("America/Grand_Turk", "SA Western Standard Time");
        map.put("America/Grenada", "SA Western Standard Time");
        map.put("America/Guadeloupe", "SA Western Standard Time");
        map.put("America/Guatemala", "Central America Standard Time");
        map.put("America/Guayaquil", "SA Pacific Standard Time");
        map.put("America/Guyana", "SA Western Standard Time");
        map.put("America/Halifax", "Atlantic Standard Time");
        map.put("America/Havana", "Eastern Standard Time");
        map.put("America/Hermosillo", "US Mountain Standard Time");
        map.put("America/Indiana/Indianapolis", "US Eastern Standard Time");
        map.put("America/Indiana/Knox", "Central Standard Time");
        map.put("America/Indiana/Marengo", "US Eastern Standard Time");
        map.put("America/Indiana/Petersburg", "Eastern Standard Time");
        map.put("America/Indiana/Tell_City", "Central Standard Time");
        map.put("America/Indiana/Vevay", "US Eastern Standard Time");
        map.put("America/Indiana/Vincennes", "Eastern Standard Time");
        map.put("America/Indiana/Winamac", "Eastern Standard Time");
        map.put("America/Indianapolis", "US Eastern Standard Time");
        map.put("America/Inuvik", "Mountain Standard Time");
        map.put("America/Iqaluit", "Eastern Standard Time");
        map.put("America/Jamaica", "SA Pacific Standard Time");
        map.put("America/Jujuy", "Argentina Standard Time");
        map.put("America/Juneau", "Alaskan Standard Time");
        map.put("America/Kentucky/Louisville", "Eastern Standard Time");
        map.put("America/Kentucky/Monticello", "Eastern Standard Time");
        map.put("America/Knox_IN", "Central Standard Time");
        map.put("America/Kralendijk", "SA Western Standard Time");
        map.put("America/La_Paz", "SA Western Standard Time");
        map.put("America/Lima", "SA Pacific Standard Time");
        map.put("America/Los_Angeles", "Pacific Standard Time");
        map.put("America/Louisville", "Eastern Standard Time");
        map.put("America/Lower_Princes", "SA Western Standard Time");
        map.put("America/Maceio", "SA Eastern Standard Time");
        map.put("America/Managua", "Central America Standard Time");
        map.put("America/Manaus", "SA Western Standard Time");
        map.put("America/Marigot", "SA Western Standard Time");
        map.put("America/Martinique", "SA Western Standard Time");
        map.put("America/Matamoros", "Central Standard Time");
        map.put("America/Mazatlan", "Mountain Standard Time (Mexico)");
        map.put("America/Mendoza", "Argentina Standard Time");
        map.put("America/Menominee", "Central Standard Time");
        map.put("America/Merida", "Central Standard Time (Mexico)");
        map.put("America/Mexico_City", "Central Standard Time (Mexico)");
        map.put("America/Moncton", "Atlantic Standard Time");
        map.put("America/Monterrey", "Central Standard Time (Mexico)");
        map.put("America/Montevideo", "Montevideo Standard Time");
        map.put("America/Montreal", "Eastern Standard Time");
        map.put("America/Montserrat", "SA Western Standard Time");
        map.put("America/Nassau", "Eastern Standard Time");
        map.put("America/New_York", "Eastern Standard Time");
        map.put("America/Nipigon", "Eastern Standard Time");
        map.put("America/Nome", "Alaskan Standard Time");
        map.put("America/Noronha", "UTC-02");
        map.put("America/North_Dakota/Beulah", "Central Standard Time");
        map.put("America/North_Dakota/Center", "Central Standard Time");
        map.put("America/North_Dakota/New_Salem", "Central Standard Time");
        map.put("America/Ojinaga", "Mountain Standard Time");
        map.put("America/Panama", "SA Pacific Standard Time");
        map.put("America/Pangnirtung", "Eastern Standard Time");
        map.put("America/Paramaribo", "SA Eastern Standard Time");
        map.put("America/Phoenix", "US Mountain Standard Time");
        map.put("America/Port-au-Prince", "Eastern Standard Time");
        map.put("America/Port_of_Spain", "SA Western Standard Time");
        map.put("America/Porto_Acre", "SA Pacific Standard Time");
        map.put("America/Porto_Velho", "SA Western Standard Time");
        map.put("America/Puerto_Rico", "SA Western Standard Time");
        map.put("America/Rainy_River", "Central Standard Time");
        map.put("America/Rankin_Inlet", "Central Standard Time");
        map.put("America/Recife", "SA Eastern Standard Time");
        map.put("America/Regina", "Canada Central Standard Time");
        map.put("America/Resolute", "Central Standard Time");
        map.put("America/Rio_Branco", "SA Pacific Standard Time");
        map.put("America/Rosario", "Argentina Standard Time");
        map.put("America/Santa_Isabel", "Pacific Standard Time (Mexico)");
        map.put("America/Santarem", "SA Eastern Standard Time");
        map.put("America/Santiago", "Pacific SA Standard Time");
        map.put("America/Santo_Domingo", "SA Western Standard Time");
        map.put("America/Sao_Paulo", "E. South America Standard Time");
        map.put("America/Scoresbysund", "Azores Standard Time");
        map.put("America/Shiprock", "Mountain Standard Time");
        map.put("America/Sitka", "Alaskan Standard Time");
        map.put("America/St_Barthelemy", "SA Western Standard Time");
        map.put("America/St_Johns", "Newfoundland Standard Time");
        map.put("America/St_Kitts", "SA Western Standard Time");
        map.put("America/St_Lucia", "SA Western Standard Time");
        map.put("America/St_Thomas", "SA Western Standard Time");
        map.put("America/St_Vincent", "SA Western Standard Time");
        map.put("America/Swift_Current", "Canada Central Standard Time");
        map.put("America/Tegucigalpa", "Central America Standard Time");
        map.put("America/Thule", "Atlantic Standard Time");
        map.put("America/Thunder_Bay", "Eastern Standard Time");
        map.put("America/Tijuana", "Pacific Standard Time");
        map.put("America/Toronto", "Eastern Standard Time");
        map.put("America/Tortola", "SA Western Standard Time");
        map.put("America/Vancouver", "Pacific Standard Time");
        map.put("America/Virgin", "SA Western Standard Time");
        map.put("America/Whitehorse", "Pacific Standard Time");
        map.put("America/Winnipeg", "Central Standard Time");
        map.put("America/Yakutat", "Alaskan Standard Time");
        map.put("America/Yellowknife", "Mountain Standard Time");
        map.put("Antarctica/Casey", "W. Australia Standard Time");
        map.put("Antarctica/Davis", "SE Asia Standard Time");
        map.put("Antarctica/DumontDUrville", "West Pacific Standard Time");
        map.put("Antarctica/Macquarie", "Central Pacific Standard Time");
        map.put("Antarctica/Mawson", "West Asia Standard Time");
        map.put("Antarctica/McMurdo", "New Zealand Standard Time");
        map.put("Antarctica/Palmer", "Pacific SA Standard Time");
        map.put("Antarctica/Rothera", "SA Eastern Standard Time");
        map.put("Antarctica/South_Pole", "New Zealand Standard Time");
        map.put("Antarctica/Syowa", "E. Africa Standard Time");
        map.put("Antarctica/Vostok", "Central Asia Standard Time");
        map.put("Arctic/Longyearbyen", "W. Europe Standard Time");
        map.put("Asia/Aden", "Arab Standard Time");
        map.put("Asia/Almaty", "Central Asia Standard Time");
        map.put("Asia/Amman", "Jordan Standard Time");
        map.put("Asia/Anadyr", "Russia Time Zone 11");
        map.put("Asia/Aqtau", "West Asia Standard Time");
        map.put("Asia/Aqtobe", "West Asia Standard Time");
        map.put("Asia/Ashgabat", "West Asia Standard Time");
        map.put("Asia/Ashkhabad", "West Asia Standard Time");
        map.put("Asia/Baghdad", "Arabic Standard Time");
        map.put("Asia/Bahrain", "Arab Standard Time");
        map.put("Asia/Baku", "Azerbaijan Standard Time");
        map.put("Asia/Bangkok", "SE Asia Standard Time");
        map.put("Asia/Beirut", "Middle East Standard Time");
        map.put("Asia/Bishkek", "Central Asia Standard Time");
        map.put("Asia/Brunei", "Singapore Standard Time");
        map.put("Asia/Calcutta", "India Standard Time");
        map.put("Asia/Chita", "North Asia East Standard Time");
        map.put("Asia/Choibalsan", "Ulaanbaatar Standard Time");
        map.put("Asia/Chongqing", "China Standard Time");
        map.put("Asia/Chungking", "China Standard Time");
        map.put("Asia/Colombo", "Sri Lanka Standard Time");
        map.put("Asia/Dacca", "Bangladesh Standard Time");
        map.put("Asia/Damascus", "Syria Standard Time");
        map.put("Asia/Dhaka", "Bangladesh Standard Time");
        map.put("Asia/Dili", "Tokyo Standard Time");
        map.put("Asia/Dubai", "Arabian Standard Time");
        map.put("Asia/Dushanbe", "West Asia Standard Time");
        map.put("Asia/Harbin", "China Standard Time");
        map.put("Asia/Ho_Chi_Minh", "SE Asia Standard Time");
        map.put("Asia/Hong_Kong", "China Standard Time");
        map.put("Asia/Hovd", "SE Asia Standard Time");
        map.put("Asia/Irkutsk", "North Asia East Standard Time");
        map.put("Asia/Istanbul", "Turkey Standard Time");
        map.put("Asia/Jakarta", "SE Asia Standard Time");
        map.put("Asia/Jayapura", "Tokyo Standard Time");
        map.put("Asia/Jerusalem", "Israel Standard Time");
        map.put("Asia/Kabul", "Afghanistan Standard Time");
        map.put("Asia/Kamchatka", "Russia Time Zone 11");
        map.put("Asia/Karachi", "Pakistan Standard Time");
        map.put("Asia/Kashgar", "Central Asia Standard Time");
        map.put("Asia/Kathmandu", "Nepal Standard Time");
        map.put("Asia/Katmandu", "Nepal Standard Time");
        map.put("Asia/Khandyga", "Yakutsk Standard Time");
        map.put("Asia/Kolkata", "India Standard Time");
        map.put("Asia/Krasnoyarsk", "North Asia Standard Time");
        map.put("Asia/Kuala_Lumpur", "Singapore Standard Time");
        map.put("Asia/Kuching", "Singapore Standard Time");
        map.put("Asia/Kuwait", "Arab Standard Time");
        map.put("Asia/Macao", "China Standard Time");
        map.put("Asia/Macau", "China Standard Time");
        map.put("Asia/Magadan", "Magadan Standard Time");
        map.put("Asia/Makassar", "Singapore Standard Time");
        map.put("Asia/Manila", "Singapore Standard Time");
        map.put("Asia/Muscat", "Arabian Standard Time");
        map.put("Asia/Nicosia", "GTB Standard Time");
        map.put("Asia/Novokuznetsk", "North Asia Standard Time");
        map.put("Asia/Novosibirsk", "N. Central Asia Standard Time");
        map.put("Asia/Omsk", "N. Central Asia Standard Time");
        map.put("Asia/Oral", "West Asia Standard Time");
        map.put("Asia/Phnom_Penh", "SE Asia Standard Time");
        map.put("Asia/Pontianak", "SE Asia Standard Time");
        map.put("Asia/Pyongyang", "Korea Standard Time");
        map.put("Asia/Qatar", "Arab Standard Time");
        map.put("Asia/Qyzylorda", "Central Asia Standard Time");
        map.put("Asia/Rangoon", "Myanmar Standard Time");
        map.put("Asia/Riyadh", "Arab Standard Time");
        map.put("Asia/Saigon", "SE Asia Standard Time");
        map.put("Asia/Sakhalin", "Vladivostok Standard Time");
        map.put("Asia/Samarkand", "West Asia Standard Time");
        map.put("Asia/Seoul", "Korea Standard Time");
        map.put("Asia/Shanghai", "China Standard Time");
        map.put("Asia/Singapore", "Singapore Standard Time");
        map.put("Asia/Srednekolymsk", "Russia Time Zone 10");
        map.put("Asia/Taipei", "Taipei Standard Time");
        map.put("Asia/Tashkent", "West Asia Standard Time");
        map.put("Asia/Tbilisi", "Georgian Standard Time");
        map.put("Asia/Tehran", "Iran Standard Time");
        map.put("Asia/Tel_Aviv", "Israel Standard Time");
        map.put("Asia/Thimbu", "Bangladesh Standard Time");
        map.put("Asia/Thimphu", "Bangladesh Standard Time");
        map.put("Asia/Tokyo", "Tokyo Standard Time");
        map.put("Asia/Ujung_Pandang", "Singapore Standard Time");
        map.put("Asia/Ulaanbaatar", "Ulaanbaatar Standard Time");
        map.put("Asia/Ulan_Bator", "Ulaanbaatar Standard Time");
        map.put("Asia/Urumqi", "Central Asia Standard Time");
        map.put("Asia/Ust-Nera", "Vladivostok Standard Time");
        map.put("Asia/Vientiane", "SE Asia Standard Time");
        map.put("Asia/Vladivostok", "Vladivostok Standard Time");
        map.put("Asia/Yakutsk", "Yakutsk Standard Time");
        map.put("Asia/Yekaterinburg", "Ekaterinburg Standard Time");
        map.put("Asia/Yerevan", "Caucasus Standard Time");
        map.put("Atlantic/Azores", "Azores Standard Time");
        map.put("Atlantic/Bermuda", "Atlantic Standard Time");
        map.put("Atlantic/Canary", "GMT Standard Time");
        map.put("Atlantic/Cape_Verde", "Cape Verde Standard Time");
        map.put("Atlantic/Faeroe", "GMT Standard Time");
        map.put("Atlantic/Faroe", "GMT Standard Time");
        map.put("Atlantic/Jan_Mayen", "W. Europe Standard Time");
        map.put("Atlantic/Madeira", "GMT Standard Time");
        map.put("Atlantic/Reykjavik", "Greenwich Standard Time");
        map.put("Atlantic/South_Georgia", "UTC-02");
        map.put("Atlantic/St_Helena", "Greenwich Standard Time");
        map.put("Atlantic/Stanley", "SA Eastern Standard Time");
        map.put("Australia/ACT", "AUS Eastern Standard Time");
        map.put("Australia/Adelaide", "Cen. Australia Standard Time");
        map.put("Australia/Brisbane", "E. Australia Standard Time");
        map.put("Australia/Broken_Hill", "Cen. Australia Standard Time");
        map.put("Australia/Canberra", "AUS Eastern Standard Time");
        map.put("Australia/Currie", "Tasmania Standard Time");
        map.put("Australia/Darwin", "AUS Central Standard Time");
        map.put("Australia/Hobart", "Tasmania Standard Time");
        map.put("Australia/Lindeman", "E. Australia Standard Time");
        map.put("Australia/Melbourne", "AUS Eastern Standard Time");
        map.put("Australia/NSW", "AUS Eastern Standard Time");
        map.put("Australia/North", "AUS Central Standard Time");
        map.put("Australia/Perth", "W. Australia Standard Time");
        map.put("Australia/Queensland", "E. Australia Standard Time");
        map.put("Australia/South", "Cen. Australia Standard Time");
        map.put("Australia/Sydney", "AUS Eastern Standard Time");
        map.put("Australia/Tasmania", "Tasmania Standard Time");
        map.put("Australia/Victoria", "AUS Eastern Standard Time");
        map.put("Australia/West", "W. Australia Standard Time");
        map.put("Australia/Yancowinna", "Cen. Australia Standard Time");
        map.put("Brazil/Acre", "SA Pacific Standard Time");
        map.put("Brazil/DeNoronha", "UTC-02");
        map.put("Brazil/East", "E. South America Standard Time");
        map.put("Brazil/West", "SA Western Standard Time");
        map.put("CST6CDT", "Central Standard Time");
        map.put("Canada/Atlantic", "Atlantic Standard Time");
        map.put("Canada/Central", "Central Standard Time");
        map.put("Canada/East-Saskatchewan", "Canada Central Standard Time");
        map.put("Canada/Eastern", "Eastern Standard Time");
        map.put("Canada/Mountain", "Mountain Standard Time");
        map.put("Canada/Newfoundland", "Newfoundland Standard Time");
        map.put("Canada/Pacific", "Pacific Standard Time");
        map.put("Canada/Saskatchewan", "Canada Central Standard Time");
        map.put("Canada/Yukon", "Pacific Standard Time");
        map.put("Chile/Continental", "Pacific SA Standard Time");
        map.put("Cuba", "Eastern Standard Time");
        map.put("EST", "SA Pacific Standard Time");
        map.put("EST5EDT", "Eastern Standard Time");
        map.put("Egypt", "Egypt Standard Time");
        map.put("Eire", "GMT Standard Time");
        map.put("Etc/GMT", "UTC");
        map.put("Etc/GMT+0", "UTC");
        map.put("Etc/GMT+1", "Cape Verde Standard Time");
        map.put("Etc/GMT+10", "Hawaiian Standard Time");
        map.put("Etc/GMT+11", "UTC-11");
        map.put("Etc/GMT+12", "Dateline Standard Time");
        map.put("Etc/GMT+2", "UTC-02");
        map.put("Etc/GMT+3", "SA Eastern Standard Time");
        map.put("Etc/GMT+4", "SA Western Standard Time");
        map.put("Etc/GMT+5", "SA Pacific Standard Time");
        map.put("Etc/GMT+6", "Central America Standard Time");
        map.put("Etc/GMT+7", "US Mountain Standard Time");
        map.put("Etc/GMT-0", "UTC");
        map.put("Etc/GMT-1", "W. Central Africa Standard Time");
        map.put("Etc/GMT-10", "West Pacific Standard Time");
        map.put("Etc/GMT-11", "Central Pacific Standard Time");
        map.put("Etc/GMT-12", "UTC+12");
        map.put("Etc/GMT-13", "Tonga Standard Time");
        map.put("Etc/GMT-14", "Line Islands Standard Time");
        map.put("Etc/GMT-2", "South Africa Standard Time");
        map.put("Etc/GMT-3", "E. Africa Standard Time");
        map.put("Etc/GMT-4", "Arabian Standard Time");
        map.put("Etc/GMT-5", "West Asia Standard Time");
        map.put("Etc/GMT-6", "Central Asia Standard Time");
        map.put("Etc/GMT-7", "SE Asia Standard Time");
        map.put("Etc/GMT-8", "Singapore Standard Time");
        map.put("Etc/GMT-9", "Tokyo Standard Time");
        map.put("Etc/GMT0", "UTC");
        map.put("Etc/Greenwich", "UTC");
        map.put("Etc/UCT", "UTC");
        map.put("Etc/UTC", "UTC");
        map.put("Etc/Universal", "UTC");
        map.put("Etc/Zulu", "UTC");
        map.put("Europe/Amsterdam", "W. Europe Standard Time");
        map.put("Europe/Andorra", "W. Europe Standard Time");
        map.put("Europe/Athens", "GTB Standard Time");
        map.put("Europe/Belfast", "GMT Standard Time");
        map.put("Europe/Belgrade", "Central Europe Standard Time");
        map.put("Europe/Berlin", "W. Europe Standard Time");
        map.put("Europe/Bratislava", "Central Europe Standard Time");
        map.put("Europe/Brussels", "Romance Standard Time");
        map.put("Europe/Bucharest", "GTB Standard Time");
        map.put("Europe/Budapest", "Central Europe Standard Time");
        map.put("Europe/Busingen", "W. Europe Standard Time");
        map.put("Europe/Chisinau", "GTB Standard Time");
        map.put("Europe/Copenhagen", "Romance Standard Time");
        map.put("Europe/Dublin", "GMT Standard Time");
        map.put("Europe/Gibraltar", "W. Europe Standard Time");
        map.put("Europe/Guernsey", "GMT Standard Time");
        map.put("Europe/Helsinki", "FLE Standard Time");
        map.put("Europe/Isle_of_Man", "GMT Standard Time");
        map.put("Europe/Istanbul", "Turkey Standard Time");
        map.put("Europe/Jersey", "GMT Standard Time");
        map.put("Europe/Kaliningrad", "Kaliningrad Standard Time");
        map.put("Europe/Kiev", "FLE Standard Time");
        map.put("Europe/Lisbon", "GMT Standard Time");
        map.put("Europe/Ljubljana", "Central Europe Standard Time");
        map.put("Europe/London", "GMT Standard Time");
        map.put("Europe/Luxembourg", "W. Europe Standard Time");
        map.put("Europe/Madrid", "Romance Standard Time");
        map.put("Europe/Malta", "W. Europe Standard Time");
        map.put("Europe/Mariehamn", "FLE Standard Time");
        map.put("Europe/Minsk", "Belarus Standard Time");
        map.put("Europe/Monaco", "W. Europe Standard Time");
        map.put("Europe/Moscow", "Russian Standard Time");
        map.put("Europe/Nicosia", "GTB Standard Time");
        map.put("Europe/Oslo", "W. Europe Standard Time");
        map.put("Europe/Paris", "Romance Standard Time");
        map.put("Europe/Podgorica", "Central Europe Standard Time");
        map.put("Europe/Prague", "Central Europe Standard Time");
        map.put("Europe/Riga", "FLE Standard Time");
        map.put("Europe/Rome", "W. Europe Standard Time");
        map.put("Europe/Samara", "Russia Time Zone 3");
        map.put("Europe/San_Marino", "W. Europe Standard Time");
        map.put("Europe/Sarajevo", "Central European Standard Time");
        map.put("Europe/Simferopol", "Russian Standard Time");
        map.put("Europe/Skopje", "Central European Standard Time");
        map.put("Europe/Sofia", "FLE Standard Time");
        map.put("Europe/Stockholm", "W. Europe Standard Time");
        map.put("Europe/Tallinn", "FLE Standard Time");
        map.put("Europe/Tirane", "Central Europe Standard Time");
        map.put("Europe/Tiraspol", "GTB Standard Time");
        map.put("Europe/Uzhgorod", "FLE Standard Time");
        map.put("Europe/Vaduz", "W. Europe Standard Time");
        map.put("Europe/Vatican", "W. Europe Standard Time");
        map.put("Europe/Vienna", "W. Europe Standard Time");
        map.put("Europe/Vilnius", "FLE Standard Time");
        map.put("Europe/Volgograd", "Russian Standard Time");
        map.put("Europe/Warsaw", "Central European Standard Time");
        map.put("Europe/Zagreb", "Central European Standard Time");
        map.put("Europe/Zaporozhye", "FLE Standard Time");
        map.put("Europe/Zurich", "W. Europe Standard Time");
        map.put("GB", "GMT Standard Time");
        map.put("GB-Eire", "GMT Standard Time");
        map.put("GMT", "UTC");
        map.put("GMT+0", "UTC");
        map.put("GMT-0", "UTC");
        map.put("GMT0", "UTC");
        map.put("Greenwich", "UTC");
        map.put("HST", "Hawaiian Standard Time");
        map.put("Hongkong", "China Standard Time");
        map.put("Iceland", "Greenwich Standard Time");
        map.put("Indian/Antananarivo", "E. Africa Standard Time");
        map.put("Indian/Chagos", "Central Asia Standard Time");
        map.put("Indian/Christmas", "SE Asia Standard Time");
        map.put("Indian/Cocos", "Myanmar Standard Time");
        map.put("Indian/Comoro", "E. Africa Standard Time");
        map.put("Indian/Kerguelen", "West Asia Standard Time");
        map.put("Indian/Mahe", "Mauritius Standard Time");
        map.put("Indian/Maldives", "West Asia Standard Time");
        map.put("Indian/Mauritius", "Mauritius Standard Time");
        map.put("Indian/Mayotte", "E. Africa Standard Time");
        map.put("Indian/Reunion", "Mauritius Standard Time");
        map.put("Iran", "Iran Standard Time");
        map.put("Israel", "Israel Standard Time");
        map.put("Jamaica", "SA Pacific Standard Time");
        map.put("Japan", "Tokyo Standard Time");
        map.put("Kwajalein", "UTC+12");
        map.put("Libya", "Libya Standard Time");
        map.put("MST", "US Mountain Standard Time");
        map.put("MST7MDT", "Mountain Standard Time");
        map.put("Mexico/BajaNorte", "Pacific Standard Time");
        map.put("Mexico/BajaSur", "Mountain Standard Time (Mexico)");
        map.put("Mexico/General", "Central Standard Time (Mexico)");
        map.put("NZ", "New Zealand Standard Time");
        map.put("Navajo", "Mountain Standard Time");
        map.put("PRC", "China Standard Time");
        map.put("PST8PDT", "Pacific Standard Time");
        map.put("Pacific/Apia", "Samoa Standard Time");
        map.put("Pacific/Auckland", "New Zealand Standard Time");
        map.put("Pacific/Bougainville", "Central Pacific Standard Time");
        map.put("Pacific/Chuuk", "West Pacific Standard Time");
        map.put("Pacific/Efate", "Central Pacific Standard Time");
        map.put("Pacific/Enderbury", "Tonga Standard Time");
        map.put("Pacific/Fakaofo", "Tonga Standard Time");
        map.put("Pacific/Fiji", "Fiji Standard Time");
        map.put("Pacific/Funafuti", "UTC+12");
        map.put("Pacific/Galapagos", "Central America Standard Time");
        map.put("Pacific/Guadalcanal", "Central Pacific Standard Time");
        map.put("Pacific/Guam", "West Pacific Standard Time");
        map.put("Pacific/Honolulu", "Hawaiian Standard Time");
        map.put("Pacific/Johnston", "Hawaiian Standard Time");
        map.put("Pacific/Kiritimati", "Line Islands Standard Time");
        map.put("Pacific/Kosrae", "Central Pacific Standard Time");
        map.put("Pacific/Kwajalein", "UTC+12");
        map.put("Pacific/Majuro", "UTC+12");
        map.put("Pacific/Midway", "UTC-11");
        map.put("Pacific/Nauru", "UTC+12");
        map.put("Pacific/Niue", "UTC-11");
        map.put("Pacific/Noumea", "Central Pacific Standard Time");
        map.put("Pacific/Pago_Pago", "UTC-11");
        map.put("Pacific/Palau", "Tokyo Standard Time");
        map.put("Pacific/Pohnpei", "Central Pacific Standard Time");
        map.put("Pacific/Ponape", "Central Pacific Standard Time");
        map.put("Pacific/Port_Moresby", "West Pacific Standard Time");
        map.put("Pacific/Rarotonga", "Hawaiian Standard Time");
        map.put("Pacific/Saipan", "West Pacific Standard Time");
        map.put("Pacific/Samoa", "UTC-11");
        map.put("Pacific/Tahiti", "Hawaiian Standard Time");
        map.put("Pacific/Tarawa", "UTC+12");
        map.put("Pacific/Tongatapu", "Tonga Standard Time");
        map.put("Pacific/Truk", "West Pacific Standard Time");
        map.put("Pacific/Wake", "UTC+12");
        map.put("Pacific/Wallis", "UTC+12");
        map.put("Pacific/Yap", "West Pacific Standard Time");
        map.put("Poland", "Central European Standard Time");
        map.put("Portugal", "GMT Standard Time");
        map.put("ROC", "Taipei Standard Time");
        map.put("ROK", "Korea Standard Time");
        map.put("Singapore", "Singapore Standard Time");
        map.put("Turkey", "Turkey Standard Time");
        map.put("UCT", "UTC");
        map.put("US/Alaska", "Alaskan Standard Time");
        map.put("US/Arizona", "US Mountain Standard Time");
        map.put("US/Central", "Central Standard Time");
        map.put("US/East-Indiana", "US Eastern Standard Time");
        map.put("US/Eastern", "Eastern Standard Time");
        map.put("US/Hawaii", "Hawaiian Standard Time");
        map.put("US/Indiana-Starke", "Central Standard Time");
        map.put("US/Michigan", "Eastern Standard Time");
        map.put("US/Mountain", "Mountain Standard Time");
        map.put("US/Pacific", "Pacific Standard Time");
        map.put("US/Pacific-New", "Pacific Standard Time");
        map.put("US/Samoa", "UTC-11");
        map.put("UTC", "UTC");
        map.put("Universal", "UTC");
        map.put("W-SU", "Russian Standard Time");
        map.put("Zulu", "UTC");
        //additions outside of Unicode list
        map.put("America/Adak", "Hawaiian Standard Time,(UTC-10:00) Hawaii");
        map.put("America/Atka", "Hawaiian Standard Time,(UTC-10:00) Hawaii");
        map.put("America/Metlakatla", "Pacific Standard Time");
        map.put("America/Miquelon", "South America Standard Time");
        map.put("Asia/Gaza", "Middle East Standard Time");
        return map;
    }

}
