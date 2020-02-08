# Hexgrid
Hexgrid generator

Generates a hexgrid by dividing each pixel into an 10x17 small grid and an 3x2 big grid (of the smaller grid) and returns the coordinate to the corresponding hex cell.

Note: only use positive coordinates or the algorithm will mess up! I have to fix that bug someday...  

Algorithm:

```Java
//This is the side length of the hexagon. You can freely change this.
private static final float HEX_GRID_SIZE = 30f;

//Keep these as they are
private static final float HEX_GRID_FACTOR_X = HEX_GRID_SIZE / 2f;
private static final float HEX_GRID_FACTOR_Y = (float) (HEX_GRID_SIZE / 2f * Math.tan(Math.toRadians(60)));

private static int[] getHexGrid(int px, int py) {
	int sgx = (int) (px / HEX_GRID_FACTOR_X);
	int sgy = (int) (py / HEX_GRID_FACTOR_Y);
	
	int bgx = sgx / 3;
	int bgy = sgy / 2;
	
	if (sgx % 3 == 2) {//If you are in the right third of the big grid
		float ipx = px / HEX_GRID_FACTOR_X - sgx;//Internal positions inside the small grid cells in percent
		float ipy = py / HEX_GRID_FACTOR_Y - sgy;
		
		// (bgx & 1) == (sgy & 1) is a "chess - pattern" check
		// if true you have an edge from the upper-left to lower-right corner 
		// otherwise you have an edge from the lower-left to upper-right corner.
		// Then it checks if you are part of the cell belonging to the left hex grid cell or the right
		// hex grid cell and updates the big grid coordinate accordingly.
		if (ipx > ((bgx & 1) == (sgy & 1)? ipy : (1 - ipy))) {
			//belongs to the hex grid cell to the right
			bgx += 1;
		}
	}
		
	if ((bgx & 1) == 0) {//if you are in an
		//even column: return the coordinates as is
		return new int[] {bgx, bgy};
	} else {
		//uneven column: add one to the big grid y coordinate
		//		 if you are in the lower part of the big grid cell
		return new int[] {bgx, bgy + ((sgy & 1) == 0? 0 : 1)};
	}
}
```
Hexgrid with small and big grid visible:<br>
![alt text](https://github.com/nullpointerException101/Hexgrid/blob/master/Grid.png "Red: small grid; Black: big grid;")

<br/><a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
