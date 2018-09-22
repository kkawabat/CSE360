
public class pathAndtotalDuration implements Comparable<pathAndtotalDuration>{

	public String path;
	public int duration;
	
	public pathAndtotalDuration(String path, int duration) {
		this.path = path;
		this.duration = duration;
	}

	@Override
	public int compareTo(pathAndtotalDuration B) {
		return B.duration - this.duration;
	}
	
	@Override
	public String toString() {
		return this.path + " Duration:" + Integer.toString(this.duration);
	}

}
