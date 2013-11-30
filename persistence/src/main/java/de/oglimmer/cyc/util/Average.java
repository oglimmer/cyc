package de.oglimmer.cyc.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Average {

	private @Getter
	@Setter
	long total;

	private @Getter
	@Setter
	int num;

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
