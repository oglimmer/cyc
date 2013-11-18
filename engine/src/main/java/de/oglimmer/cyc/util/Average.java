package de.oglimmer.cyc.util;

public class Average {

	private long total;
	private int num;

	public Average() {
	}

	public Average(long value) {
		total = value;
		num = 1;
	}

	public void add(long i) {
		total += i;
		num++;
	}

	public double average() {
		return total / num;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
