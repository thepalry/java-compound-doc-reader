package compoundFile.util;

public class TimeStamp {
	private static final int[] DAYS_OF_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private long secFraction;
	private int year, month, day, hour, min, sec;

	public TimeStamp(long time) {
		secFraction = time % 10000000;
		time /= 10000000;
		sec = (int) (time % 60);
		time /= 60;
		min = (int) (time % 60);
		time /= 60;
		hour = (int) (time % 24);
		time /= 24;
		year = 1601;
		boolean leapYear = isLeapYear(year);
		while (true) {
			int daysOfYear = (leapYear) ? 366 : 365;
			if (time <= daysOfYear) {
				break;
			}
			year++;
			time -= daysOfYear;
		}
		month = 1;
		while (true) {
			int daysOfMonth = DAYS_OF_MONTH[month - 1];
			if (month == 2 && leapYear) {
				daysOfMonth++;
			}
			if (time <= daysOfMonth) {
				break;
			}
			month++;
			time -= daysOfMonth;
		}
		day = (int) time;
	}

	public long getSecFraction() {
		return secFraction;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMin() {
		return min;
	}

	public int getSec() {
		return sec;
	}

	private boolean isLeapYear(int year) {
		if (year % 400 == 0) {
			return true;
		}
		if (year % 100 == 0) {
			return false;
		}
		if (year % 4 == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return year + "." + month + "." + day + " " + hour + ":" + min + ":" + sec;
	}
}
