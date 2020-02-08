package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {
	
	private static final int ALPHA_BYTE = 3;
	private static final int RED_BYTE   = 2;
	private static final int GREEN_BYTE = 1;
	private static final int BLUE_BYTE  = 0;
	
	private static final int BLACK = 0xFF << (ALPHA_BYTE * 8);
	private static final int RED   = BLACK | (0xFF << (RED_BYTE   * 8));
	private static final int GREEN = BLACK | (0xFF << (GREEN_BYTE * 8));
	private static final int BLUE  = BLACK | (0xFF << (BLUE_BYTE  * 8));
	
	private static final int DARK_RED   = BLACK | (0x7F << (RED_BYTE   * 8));
	private static final int DARK_GREEN = BLACK | (0x7F << (GREEN_BYTE * 8));
	private static final int DARK_BLUE  = BLACK | (0x7F << (BLUE_BYTE  * 8));
	
	//private static final int WHITE   = RED   | GREEN | BLUE;
	private static final int PINK    = RED   | BLUE;
	private static final int CYAN    = GREEN | BLUE;
	private static final int YELLOW  = RED   | GREEN;
	
	//private static final int GREY         = DARK_RED   | DARK_GREEN | DARK_BLUE;
	private static final int DARK_PINK    = DARK_RED   | DARK_BLUE;
	private static final int DARK_CYAN    = DARK_GREEN | DARK_BLUE;
	private static final int DARK_YELLOW  = DARK_RED   | DARK_GREEN;
	
	private static final int LIGHT_PINK   = RED | DARK_GREEN | BLUE;
	private static final int LIGHT_CYAN   = DARK_RED | GREEN | BLUE;
	private static final int LIGHT_YELLOW = RED | GREEN | DARK_BLUE;
	
	private static final boolean[][][] DIGIT_MASKS = {//[10][7][5]
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, true , false, true , false},
        	{false, true , false, true , false},
        	{false, true , false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, false, false, true , false},
        	{false, false, true , true , false},
        	{false, false, false, true , false},
        	{false, false, false, true , false},
        	{false, false, false, true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, false, false, true , false},
        	{false, true , true , true , false},
        	{false, true , false, false, false},
        	{false, true , true , true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, false, false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , false, true , false},
        	{false, true , false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, true , false},
        	{false, false, false, true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, true , false, false, false},
        	{false, true , true , true , false},
        	{false, false, false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, true , false, false, false},
        	{false, true , true , true , false},
        	{false, true , false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, false, false, true , false},
        	{false, false, false, true , false},
        	{false, false, false, true , false},
        	{false, false, false, true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, true , false, true , false},
        	{false, true , true , true , false},
        	{false, true , false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, false, false}
        },
        {
        	{false, false, false, false, false},
        	{false, true , true , true , false},
        	{false, true , false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, true , false},
        	{false, true , true , true , false},
        	{false, false, false, false, false}
        }
    };
	
	private static final int IMAGE_WIDTH = 1024;
	private static final int IMAGE_HEIGHT = 1024;
	
	private static final float HEX_GRID_SIZE = 30f;//>= 10 for good results / 25 when using text 
	
	private static final float HEX_GRID_FACTOR_X = HEX_GRID_SIZE / 2f;
	private static final float HEX_GRID_FACTOR_Y = (float) (HEX_GRID_SIZE / 2f * Math.tan(Math.toRadians(60)));
	
	public static void main(String[] args) {
		BufferedImage bufferdImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);//0xAARRGGBB
		
		int[][] data = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
		int[] color = new int[]{PINK,             CYAN,       YELLOW, 
								DARK_PINK,   DARK_CYAN,  DARK_YELLOW, 
								LIGHT_PINK, LIGHT_CYAN, LIGHT_YELLOW, BLACK};
		
		//generate colors
		for (int px = 0; px < IMAGE_WIDTH; px++) {
			for (int py = 0; py < IMAGE_HEIGHT; py++) {
				int[] hg = getHexGrid(px, py);
				
				//choose grid color based on coordinates
				if (hg[0] == 0 || hg[0] >= (int) (IMAGE_WIDTH / (3 * HEX_GRID_FACTOR_X)) || hg[1] == 0 || hg[1] >= (int) (IMAGE_HEIGHT / (2 * HEX_GRID_FACTOR_Y))) {
					data[px][py] = 9;
				} else {
					data[px][py] = (hg[1] + ((hg[0] & 1) == 0? 0 : 1)) % 3;
				}
			}
		}
		
		//shade edges
		for (int px = 0; px < IMAGE_WIDTH; px++) {
			for (int py = 0; py < IMAGE_HEIGHT; py++) {
				if (px != 0 && py != 0 && px != IMAGE_WIDTH - 1 && py != IMAGE_HEIGHT -1 && data[px][py] != 9) {
					int cindex = data[px][py] % 3;
				
					boolean top    = py > 0            && (data[px  ][py-1] % 3 != cindex);
					boolean bottom = py < IMAGE_HEIGHT && (data[px  ][py+1] % 3 != cindex);
					boolean left   = px > 0            && (data[px-1][py  ] % 3 != cindex);
					boolean right  = px < IMAGE_WIDTH  && (data[px+1][py  ] % 3 != cindex);
					
					if (top || bottom || left || right) {
						cindex += 3;
					}
					
					data[px][py] = cindex;
				}
			}
		}
		
		//render text
		if (HEX_GRID_SIZE >= 19) {
			for (int gx = 0; gx <= IMAGE_WIDTH / HEX_GRID_FACTOR_X; gx++) {
				for (int gy = 0; gy <= IMAGE_HEIGHT / HEX_GRID_FACTOR_Y; gy++) {
					String str = (gx - 1) + " | " + (gy - 1);
					
					final int px = (int) (gx * 3 * HEX_GRID_FACTOR_X + HEX_GRID_FACTOR_X - (str.length() * 5f / 2f));
					final int py = (int) (gy * 2 * HEX_GRID_FACTOR_Y - ((gx & 1) == 1? HEX_GRID_FACTOR_Y : 0) + HEX_GRID_FACTOR_Y - 3);
					
					for (int n = 0; n < str.length(); n++) {
						for (int k = 0; k < 7; k++) {
							for (int m = 0; m < 5; m++) {
								int ix = px + 5 * n + m;
								int iy = py + k;
								
								if (ix >= 0 && ix < IMAGE_WIDTH && iy >= 0 && iy < IMAGE_HEIGHT) {
									switch (str.charAt(n)) {
										case '|':
										{
											if (m == 2 && k > 0 && k < 6) {
												data[ix][iy] = 9;
											}
										} break;
										case '-':
										{
											if (k == 3 && m > 0 && m < 4) {
												data[ix][iy] = 9;
											}
										} break;
										
										case '0': case '1': case '2': case '3': case '4':
										case '5': case '6': case '7': case '8': case '9':
										{
											int i = Character.getNumericValue(str.charAt(n));
											
											if (DIGIT_MASKS[i][k][m]) {
												data[ix][iy] = 9;
											}
										} break;
										
										default:
											break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		//render image
		for (int px = 0; px < IMAGE_WIDTH; px++) {
			for (int py = 0; py < IMAGE_HEIGHT; py++) {
				bufferdImage.setRGB(px, py, color[data[px][py]]);
			}
		}
		
		try {
			ImageIO.write(bufferdImage, "PNG", new File("Test2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
}
