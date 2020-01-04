# Hexgrid
Hexgrid generator

Generates a hexgrid by dividing each pixel into an 10x17 small grid and an 3x2 big grid (of the smaller grid) and returns the coordinate to the corresponding hex cell.

Algorithm:

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
