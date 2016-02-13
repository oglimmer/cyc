package de.oglimmer.cyc.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class Average {

	@Getter
	@Setter
	private long total;

	@Getter
	@Setter
	private int num;

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

}
