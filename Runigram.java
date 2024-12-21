import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
	
		// Read the PPM header
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt(); 
		in.readInt(); 
	
		// Create the 2D array to store the image
		Color[][] image = new Color[numRows][numCols];
	
		// Read pixel data into the array
		for (int i = 0; i < numRows; i++) { 
			for (int j = 0; j < numCols; j++) { 
				int r = in.readInt();
				int g = in.readInt(); 
				int b = in.readInt();
	
				image[i][j] = new Color(r, g, b);
			}
		}
	
		return image;
	}
	

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				print(image[i][j]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		int numRows = image.length;
		int numCols = image[0].length;
		Color[][] newImg = new Color[numRows][numCols];
	
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) { 
				newImg[i][j] = image[i][numCols - 1 - j];
			}
		}
	
		return newImg;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image) {
		int numRows = image.length;
		int numCols = image[0].length;
		Color[][] newImg = new Color[numRows][numCols];
	
		for (int i = 0; i < numRows; i++) { 
			for (int j = 0; j < numCols; j++) { 
				newImg[i][j] = image[numRows - 1 - i][j];
			}
		}
	
		return newImg;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	  // Computes the luminance of the RGB values of the given pixel.
	  private static Color luminance(Color pixel) {
        int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();

        int lum = (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b);

        // Ensure luminance is within the valid color range [0, 255]
        lum = Math.max(0, Math.min(255, lum));

        return new Color(lum, lum, lum);
    }

    /** Returns an image which is the grayscale version of the given image. */
    public static Color[][] grayScaled(Color[][] image) {
        int numRows = image.length;
        int numCols = image[0].length;

        Color[][] grayImage = new Color[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grayImage[i][j] = luminance(image[i][j]);
            }
        }

        return grayImage;
    }
    /** Returns an image which is the scaled version of the given image. */
    public static Color[][] scaled(Color[][] image, int width, int height) {
        int numRows = image.length;
        int numCols = image[0].length;
        Color[][] newImg = new Color[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Map the new (i, j) to the old image's (x, y)
                int x = j * numCols / width;
                int y = i * numRows / height;
                newImg[i][j] = image[y][x];
            }
        }

        return newImg;
    }

    /** Computes and returns a blended color which is a linear combination of the two given colors. */
    public static Color blend(Color c1, Color c2, double alpha) {
        int r = (int) (alpha * c1.getRed() + (1 - alpha) * c2.getRed());
        int g = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
        int b = (int) (alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());
        return new Color(r, g, b);
    }

    /** Constructs and returns an image which is the blending of the two given images. */
    public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
        int numRows = image1.length;
        int numCols = image1[0].length;
        Color[][] blendedImage = new Color[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                blendedImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
            }
        }

        return blendedImage;
    }

    /** Morphs the source image into the target image, gradually, in n steps. */
    public static void morph(Color[][] source, Color[][] target, int n) {
        int numRows = source.length;
        int numCols = source[0].length;
        for (int step = 0; step < n; step++) {
            Color[][] morphedImage = new Color[numRows][numCols];
            double alpha = step / (double) (n - 1); // Linear interpolation

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    morphedImage[i][j] = blend(source[i][j], target[i][j], alpha);
                }
            }
            display(morphedImage);
            try {
                Thread.sleep(100); // Add a slight delay between frames to visualize the morph
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

