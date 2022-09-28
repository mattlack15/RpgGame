package ca.mattlack.rpg.math;

import ca.mattlack.rpg.util.Serializer;

import java.util.Objects;

public class IntVector2D
{

    private final int x;
    private final int y;

    public IntVector2D(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the value of x.
     *
     * @return x
     */
    public int getX()
    {
        return x;
    }

    /**
     * Get the value of y.
     *
     * @return y
     */
    public int getY()
    {
        return y;
    }

    /**
     * Add another IntVector2D to this one.
     *
     * @param other The other IntVector2D.
     * @return The sum of the two vectors.
     */
    public IntVector2D add(IntVector2D other)
    {
        return new IntVector2D(x + other.getX(), y + other.getY());
    }

    /**
     * Subtract another IntVector2D from this one.
     *
     * @param other The other IntVector2D.
     * @return The difference of the two vectors.
     */
    public IntVector2D subtract(IntVector2D other)
    {
        return new IntVector2D(x - other.getX(), y - other.getY());
    }

    /**
     * Multiply this vector by a scalar.
     *
     * @param scalar The scalar to multiply by.
     * @return The product of the two vectors.
     */
    public IntVector2D multiply(int scalar)
    {
        return new IntVector2D(x * scalar, y * scalar);
    }

    /**
     * Divide this vector by a scalar.
     *
     * @param scalar The scalar to divide by.
     * @return The quotient of the two vectors.
     */
    public IntVector2D divide(int scalar)
    {
        return new IntVector2D(x / scalar, y / scalar);
    }

    /**
     * Dot product of two vectors.
     *
     * @param other The other IntVector2D.
     * @return The dot product of the two vectors.
     */
    public int dot(IntVector2D other)
    {
        return x * other.getX() + y * other.getY();
    }

    /**
     * Length of this vector.
     *
     * @return The length of the vector.
     */
    public int length()
    {
        return (int) Math.sqrt(x * x + y * y);
    }

    /**
     * Normalize this vector.
     *
     * @return The normalized vector.
     */
    public IntVector2D normalize()
    {
        return divide(length());
    }

    /**
     * Perform a linear transformation on this vector.
     *
     * @param i The first matrix.
     * @param j The second matrix.
     * @return The product of the two matrices.
     */
    public IntVector2D transform(IntVector2D i, IntVector2D j)
    {
        return new IntVector2D(x * i.getX() + y * i.getY(), x * j.getX() + y * j.getY());
    }

    /**
     * Rotate this vector by an angle.
     *
     * @param angle The angle to rotate by.
     * @return The rotated vector.
     */
    public IntVector2D rotate(int angle)
    {
        int cos = (int) Math.cos(angle);
        int sin = (int) Math.sin(angle);

        return new IntVector2D(x * cos - y * sin, x * sin + y * cos);
    }

    @Override
    public String toString()
    {
        return "IntVector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntVector2D that = (IntVector2D) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    /**
     * Converts the IntVector2D to a Vector2D.
     */
    public Vector2D toVector2D()
    {
        return new Vector2D(x, y);
    }

    /**
     * Returns the absolute value of the IntVector2D.
     */
    public IntVector2D abs()
    {
        return new IntVector2D(Math.abs(x), Math.abs(y));
    }

    /**
     * Returns the point of the minimum x and y values of the two IntVector2D's.
     */
    public static IntVector2D minBoxPoint(IntVector2D a, IntVector2D b)
    {
        return new IntVector2D(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()));
    }

    /**
     * Returns the point with the maximum x and y values of the two IntVector2D's
     */
    public static IntVector2D maxBoxPoint(IntVector2D a, IntVector2D b)
    {
        return new IntVector2D(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()));
    }

    /**
     * Serialize this vector.
     */
    public Serializer serialize()
    {
        Serializer serializer = new Serializer();
        serializer.writeInt(x);
        serializer.writeInt(y);
        return serializer;
    }

    /**
     * Deserialize a serialized vector.
     */
    public static IntVector2D deserialize(Serializer serializer)
    {
        return new IntVector2D(serializer.readInt(), serializer.readInt());
    }
}

