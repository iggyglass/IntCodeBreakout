public class Screen
{

    private int[][] screen;
    private int width;
    private int height;
    private int score = 0;
    private boolean isDirty = false;

    public enum Tiles
    {
        Empty,
        Wall,
        Block,
        Paddle,
        Ball
    }

    public Screen(int width, int height)
    {
        this.width = width;
        this.height = height;

        screen = new int[width][height];
    }

    public int[][] GetScreen()
    {
        isDirty = false;

        return screen;
    }

    public void SetTile(int x, int y, int tile)
    {
        if (x == -1 && y == 0)
        {
            score = tile;
            return;
        }

        isDirty = true;

        screen[x][y] = tile;
    }

    public int GetWidth()
    {
        return width;
    }

    public int GetHeight()
    {
        return height;
    }

    public int GetScore()
    {
        return score;
    }

    public boolean IsDirty()
    {
        return isDirty;
    }

    public void Print() throws Exception
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                switch (Tiles.values()[screen[x][y]])
                {
                    case Ball:
                        System.out.print("o ");
                        break;
                    case Block:
                        System.out.print("# ");
                        break;
                    case Empty:
                        System.out.print("  ");
                        break;
                    case Paddle:
                        System.out.print("═ ");
                        break;
                    case Wall:
                        System.out.print("█ ");
                        break;
                    default:
                        throw new Exception("Invalid Screen Tile.");
                }
            }

            System.out.println();
        }
    }
}