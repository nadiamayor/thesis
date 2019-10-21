package graph.searcher;


public class Duo {
	String barOne;
	String barTwo;

	public Duo(String one, String two) {
		barOne = one;
		barTwo = two;
	}

	public String getBarOne() {
		return barOne;
	}

	public String getBarTwo() {
		return barTwo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barOne == null) ? 0 : barOne.hashCode());
		result = prime * result + ((barTwo == null) ? 0 : barTwo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Duo other = (Duo) obj;
		if (barOne == null) {
			if (other.barOne != null)
				return false;
		} else if (!barOne.equals(other.barOne))
			return false;
		if (barTwo == null) {
			if (other.barTwo != null)
				return false;
		} else if (!barTwo.equals(other.barTwo))
			return false;
		return true;
	}
}
