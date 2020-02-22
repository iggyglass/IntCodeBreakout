InterpreterThread th;
CycleController cc = new CycleController(17800);

ArrayStack output = new ArrayStack(3);
Screen scr = new Screen(45, 23);
float scaleFactor = 40f;
float verticalOffset = 100f;

PFont font;

long[] data;

Long input = 0l;

void setup()
{
    size(1800, 1020);

    font = createFont("pixelLCD.ttf", 50);

    textFont(font);
    textAlign(LEFT, CENTER);

    data = Parser.ParseCode(loadStrings("pgrm.txt")[0]);
    th = new InterpreterThread(data, (Long value)->display(value), ()->getInput(), ()->getCycle());
    th.start();
}

void draw()
{
    if (scr.IsDirty()) render();
    if (scr.ScoreDirty()) renderText();
    cc.AddCycles(16);

    if (keyPressed && (keyCode == LEFT ^ keyCode == RIGHT))
    {
        if (keyCode == LEFT) input = -1l;
        else input = 1l;
    }
    else
    {
        input = 0l;
    }
}

void keyReleased()
{
    if (key == ' ')
    {
        th.Kill();

        while (!th.IsDead()) {}

        cc.Reset();
        scr.ResetScore();

        th = new InterpreterThread(data, (Long value)->display(value), ()->getInput(), ()->getCycle());
        th.start();
    }
}

Void display(Long value)
{
    output.Push(value);

    if (output.GetCount() == 3)
    {
        Long[] vals = output.GetArray();

        scr.SetTile(vals[0].intValue(), vals[1].intValue(), vals[2].intValue());

        output.Clear();
    }

    return null;
}

Boolean getCycle()
{
    return cc.GetCycle();
}

Long getInput()
{
    return input;
}

void render()
{
    int[][] data = scr.GetScreen();

    noStroke();

    for (int x = 0; x < scr.GetWidth(); x++)
    {
        for (int y = 0; y < scr.GetHeight(); y++)
        {
            float scaledX = x * scaleFactor;
            float scaledY = y * scaleFactor + verticalOffset;

            switch (Screen.Tiles.values()[data[x][y]])
            {
                case Ball:
                    fill(89, 255, 252);
                    break;
                case Block:
                    fill(239, 42, 248);
                    break;
                case Empty:
                    fill(255, 255, 255);
                    break;
                case Paddle:
                    fill(0, 0, 0);
                    break;
                case Wall:
                    fill(0, 0, 0);
                    break;
            }

            rect(scaledX, scaledY, scaleFactor, scaleFactor);
        }
    }
}

void renderText()
{
    fill(0, 0, 0);
    rect(0, 0, width, verticalOffset);
    fill(255, 255, 255);

    text(String.format("SCORE: %010d", scr.GetScore()), scaleFactor, verticalOffset / 2);
}
