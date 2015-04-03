import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings("unused")
public class KenKenSolver
{
	public final double startTime;
	private KenKenComponent component;
	private int puzzleSize;
	private int numberOfCages;
	private int[][] numbers;
	private int[] cageTarget;
	private int[][] cageIdentifier;
	private char[] cageOp;
	
	public KenKenSolver(KenKenComponent kc, String fileName) throws IOException
	{
		this.startTime = System.currentTimeMillis();
		this.component = kc;
		BufferedReader input = new BufferedReader(new FileReader(fileName));
		this.puzzleSize = Integer.parseInt(input.readLine());
		this.numberOfCages = Integer.parseInt(input.readLine());
		this.numbers = new int[this.puzzleSize][this.puzzleSize];
		this.cageTarget = new int[this.numberOfCages];
		this.cageIdentifier = new int[this.puzzleSize][this.puzzleSize];
		this.cageOp = new char[this.numberOfCages];
		
		for (int i = 0; i < this.numberOfCages; i++) {
			String[] temp = input.readLine().split(",");
			int target = Integer.parseInt(temp[0]);
			char op = temp[1].charAt(0);
			int cageSize = Integer.parseInt(temp[2]);
			for (int j = 0; j < cageSize; j++) {
				temp = input.readLine().split(",");
				int row = Integer.parseInt(temp[0]);
				int col = Integer.parseInt(temp[1]);
				this.cageIdentifier[row][col] = i;
				if (j == 0) {
					this.cageTarget[i] = target;
					this.cageOp[i] = op;
				}
			}
		}
		
		input.close();
	}
	
	public boolean solve(int row, int col)
	{
		if(col == this.puzzleSize) {
			col = 0;
			row++;
		}

		// base case
		if (row == this.puzzleSize) {
			return true;
		}

		for (int i = 1; i <= this.puzzleSize; i++) {
			this.numbers[row][col] = i;
			boolean check = false;
			boolean ckRow = checkRowCol(row, col, i);
			boolean ckCage = checkCage(row, col, this.cageIdentifier[row][col]);
			if (checkRowCol(row, col, i) && checkCage(row, col, this.cageIdentifier[row][col])) {
//				System.out.println(this.toString());
				check = solve(row, col + 1);
			}

			if (check) {
				return true;
			}
			this.numbers[row][col] = 0;
		}

		return false;
	}
	
	private boolean checkRowCol(int row, int col, int num)
	{
		for (int rowIndex = 0; rowIndex < this.puzzleSize; rowIndex++) {
			if (rowIndex != row) {
				if (this.numbers[rowIndex][col] == num) {
					return false;
				}
			}
		}
		for (int colIndex = 0; colIndex < this.puzzleSize; colIndex++) {
			if (colIndex != col) {
				if (this.numbers[row][colIndex] == num) {
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean checkCage(int row, int col, int cageID)
	{
		ArrayList<Integer> cage = new ArrayList<Integer>();
		
		for (int i = 0; i < this.puzzleSize; i++) {
			for (int j = 0; j < this.puzzleSize; j++) {
				if ((this.cageIdentifier[i][j] == cageID) && (this.numbers[i][j] <= 0)) {
					return true;
				}
				if (this.cageIdentifier[i][j] == cageID) {
					cage.add(this.numbers[i][j]);
				}
			}
		}

		ArrayList<ArrayList<Integer>> allPossible = permutation(cage);
		for (ArrayList<Integer> arr : allPossible) {
			int result = arr.get(0);
			for (int i = 1; i < arr.size(); i++) {
				if (this.cageOp[this.cageIdentifier[row][col]] == '+') {
					result += arr.get(i);
				}
				else if (this.cageOp[this.cageIdentifier[row][col]] == '-') {
					result -= arr.get(i);
				}
				else if (this.cageOp[this.cageIdentifier[row][col]] == '*') {
					result *= arr.get(i);
				}
				else if (this.cageOp[this.cageIdentifier[row][col]] == '/') {
					result /= arr.get(i);
				}
			}
			if (result == this.cageTarget[cageID]) {
				return true;
			}
		}
		
		return false;
	}
	
	private ArrayList<ArrayList<Integer>> permutation(final ArrayList<Integer> list)
	{
		// TO DO
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		permutation(0, list.size()-1, list, result);
		return result;
	}
	
	private void permutation(int start, int end, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> result)
	{
		if (start == end) {
			@SuppressWarnings("unchecked")
			ArrayList<Integer> data = (ArrayList<Integer>) list.clone();
			result.add(data);
			return;
		}
		
		for (int i = start; i <= end; i++) {
			swap(list, start, i);
			permutation(start+1, end, list, result);
			swap(list, start, i);
		}
	}
	
	private void swap(ArrayList<Integer> list, int start, int end)
	{
		Integer temp = list.get(start);
		list.set(start, list.get(end));
		list.set(end, temp);
	}
	
	public String toString()
	{
		String str = "";
		for (int i = 0; i < this.numbers.length; i++) {
			for (int j = 0; j < this.numbers[i].length; j++) {
				str += this.numbers[i][j] + " ";
			}
			str += "\n";
		}
		return str;
	}
	
	public static void main(String[] args) throws IOException
	{
		JFrame frame = new JFrame();
		KenKenComponent kc = new KenKenComponent("9x9_1.txt", frame);
		KenKenSolver solver = new KenKenSolver(kc, "9x9_1.txt");
		solver.solve(0, 0);
		kc.setNumber(solver.numbers);
		System.out.println("It took " + (Math.round(((System.currentTimeMillis() - solver.startTime) / 60000) * 100.0) / 100.0) + 
				" minutes to solve this puzzle.");
	}
}
