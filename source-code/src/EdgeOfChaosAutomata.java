import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EdgeOfChaosAutomata {
	private int K;
	private int N;
	private double lambda;
	private byte[] transitions;
	private Set<List<Byte>> states;
	private List<Byte> state;
	private Random random;
	
	public double getLambda() {
		return lambda;
	}
	
	public EdgeOfChaosAutomata(int N, int K, double lambda, Random rand) {
		this.K = K;
		this.N = N;
		this.lambda = lambda;
		this.random = rand;
		
		transitions = new byte[(int)Math.pow(K, N)]; //this is larger than we need due to quiescence and isotrophy but is quick to write
		for(int i = 0; i<transitions.length; i++) {
			if(rand.nextDouble()>lambda) {
				transitions[i] = (byte)(rand.nextInt(K)+1);
			}
		}
	}
	
	public void initializeState(int width, int num_rand, double quiescent) {
		state = new ArrayList<Byte>(width);
		for(int i=0;i<width;i++) state.add((byte)0);
		
		
		states = new HashSet<>();
		for(int i = 0; i<num_rand; i++) {
			int j = (width-num_rand)/2 + i; //technically does'nt matter due to the % transition rule but will make matching visuals easier
			if(random.nextDouble()>quiescent) {
				transitions[j] = (byte)(random.nextInt(K)+1);
			}
		}
	}
	
	public double increaseLambda(double delta) {
		int quiescent = 0;
		for(int i = 0; i<transitions.length; i++) {
			if(transitions[i]==0) {
				quiescent ++;
			}
		}
		for(int i = 0; i<transitions.length; i++) {
			if(transitions[i]==0) {
				if(random.nextDouble()<(delta*transitions.length)/quiescent) {
					transitions[i] = (byte)(random.nextInt(K)+1);
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
	
	
	public String stateToString() {
		StringBuilder sb = new StringBuilder();
		for(byte b : state) sb.append((int)b);
		return sb.toString();
	}
	
	
	public boolean step() {
		
		if(!states.add(state)) {
			System.out.println("State Extant: \t"  + stateToString());
			return true;
		}else {
			System.out.println("State New: \t" + stateToString());
		}
		byte[] next = new byte[state.size()];
		for(int i = 0; i < state.size(); i++) {
			byte[] neighborhood = new byte[N];
			for(int j = 0; j<N; j++) {
				neighborhood[j] = state.get((i+j-N/2+state.size())%state.size()); 
			}
			next[i] = nextState(neighborhood);
		}
		
		//Copying byte arr to list...
		{
			state.clear();
			for(byte b: next) state.add(b);
		}
		return false;
	}
	
	public byte[] getState() {
		
		byte[] out = new byte[state.size()];
		for(int i=0;i<state.size();i++) out[i] = state.get(i);
		
		return out;
	}
}
