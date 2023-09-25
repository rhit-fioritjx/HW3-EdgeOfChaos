import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EdgeOfChaosAutomata {
	private int K;
	private int N;
	private double lambda;
	private byte[] transitions;
	private Set<byte[]> states;
	private byte[] state;
	
	public EdgeOfChaosAutomata(int N, int K, double lambda, Random rand) {
		this.K = K;
		this.N = N;
		this.lambda = lambda;
		transitions = new byte[(int)Math.pow(K, N)]; //this is larger than we need due to quiescence and isotrophy but is quick to write
		for(int i = 0; i<transitions.length; i++) {
			if(rand.nextDouble()>lambda) {
				transitions[i] = (byte)(rand.nextInt(K)+1);
			}
		}
	}
	
	public void initializeState(int width, int num_rand, double quiescent, Random rand) {
		state = new byte[width];
		states = new HashSet<>();
		for(int i = 0; i<num_rand; i++) {
			int j = (width-num_rand)/2 + i; //technically does'nt matter due to the % transition rule but will make matching visuals easier
			if(rand.nextDouble()>quiescent) {
				transitions[j] = (byte)(rand.nextInt(K)+1);
			}
		}
	}
	
	public double increaseLambda(double delta, Random rand) {
		int quiescent = 0;
		for(int i = 0; i<transitions.length; i++) {
			if(transitions[i]==0) {
				quiescent ++;
			}
		}
		for(int i = 0; i<transitions.length; i++) {
			if(transitions[i]==0) {
				if(rand.nextDouble()<(delta*transitions.length)/quiescent) {
					transitions[i] = (byte)(rand.nextInt(K)+1);
				}
				quiescent--;
			}
		}
		lambda += delta;
		return lambda;
	}
	
	private int getIndex(byte[] neighborhood) {
		boolean direction = false; //isotrophy condition
		for(int i = 0; i<(N/2); i++) {
			if(neighborhood[i]>neighborhood[4-i]) {
				direction = true;
			}
		}
		int index = 0;
		for(int i = 0; i<N; i++) {
			int j = i;
			if(direction) {
				j = N-1-i;
			}
			index += (int)Math.pow(neighborhood[j], i);
		}
		return index;
	}
	
	private byte nextState(byte[] neighborhood) {
		boolean quiescent = true; //quiescent condition
		for(int i = 1; i<N; i++) {
			if(neighborhood[i]!=neighborhood[0]) {
				quiescent = false;
				break;
			}
		}
		if(quiescent) {
			return neighborhood[0];
		}
		return transitions[getIndex(neighborhood)];
	}
	
	public boolean step() {
		if(!states.add(state)) {
			return true;
		}
		byte[] next = new byte[state.length];
		for(int i = 0; i < state.length; i++) {
			byte[] neighborhood = new byte[N];
			for(int j = 0; j<N; j++) {
				neighborhood[j] = state[(i+j-N/2+state.length)%state.length]; 
			}
			next[i] = nextState(neighborhood);
		}
		state = next;
		return false;
	}
	
	public byte[] getState() {
		return state;
	}
}
