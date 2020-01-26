# Hexgrid
Hexgrid generator

Generates a hexgrid by dividing each pixel into an 10x17 small grid and an 3x2 big grid (of the smaller grid) and returns the coordinate to the corresponding hex cell.

Note: only use positive coordinates!

Algorithm:

```Java
private static final float HEX_GRID_SIZE = 30f;
	
private static final float HEX_GRID_FACTOR_X = HEX_GRID_SIZE / 2f;
private static final float HEX_GRID_FACTOR_Y = (float) (HEX_GRID_SIZE / 2f * Math.tan(Math.toRadians(60)));

private static int[] getHexGrid(int px, int py) {
	int sgx = (int) (px / HEX_GRID_FACTOR_X);
	int sgy = (int) (py / HEX_GRID_FACTOR_Y);
	
	int bgx = sgx / 3;
	int bgy = sgy / 2;
	
	if (sgx % 3 == 2) {
		float ipx = px / HEX_GRID_FACTOR_X - sgx;
		float ipy = py / HEX_GRID_FACTOR_Y - sgy;
		
		if (ipx > ((bgx & 1) == (sgy & 1)? ipy : (1 - ipy))) {
			sgx += 1;
			bgx += 1;
		} else {
			sgx -= 1;
		}
	}
		
	if ((bgx & 1) == 0) {
		return new int[] {bgx, bgy};
	} else {
		return new int[] {bgx, bgy + ((sgy & 1) == 0? 0 : 1)};
	}
}
```
Hexgrid with small and big grid visible:<br>
![alt text](https://github.com/nullpointerException101/Hexgrid/blob/master/Grid.png "Red: small grid; Black: big grid;")
