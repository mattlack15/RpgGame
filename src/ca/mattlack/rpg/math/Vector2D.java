package ca.mattlack.rpg.math;

import java.util.Objects;

public class Vector2D
{
    private final double x;
    private final double y;

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of this vector.
     */
    public double getX()
    {
        return x;
    }

    /**
     * Returns the y coordinate of this vector.
     */
    public double getY()
    {
        return y;
    }

    /**
     * Returns a new vector with the x coordinate set to x.
     */
    public Vector2D setX(double x)
    {
        return new Vector2D(x, y);
    }

    /**
     * Returns a new vector with the y coordinate set to y.
     */
    public Vector2D setY(double y)
    {
        return new Vector2D(x, y);
    }

    /**
     * Returns a new vector with the x coordinate set to x + other.getX() and the y coordinate set to y + other.getY().
     */
    public Vector2D add(Vector2D other)
    {
        return new Vector2D(x + other.getX(), y + other.getY());
    }

    /**
     * Returns a new vector with the x coordinate set to this.x + x and the y coordinate set to this.y + y.
     */
    public Vector2D add(double x, double y)
    {
        return new Vector2D(this.x + x, this.y + y);
    }

    /**
     * Returns a new vector with the x coordinate set to x - other.getX() and the y coordinate set to y - other.getY().
     */
    public Vector2D subtract(Vector2D other)
    {
        return new Vector2D(x - other.getX(), y - other.getY());
    }

    /**
     * Returns a new version of this vector but scaled by the supplied scalar.
     */
    public Vector2D multiply(double scalar)
    {
        return new Vector2D(x * scalar, y * scalar);
    }

    /**
     * Returns a new vector that is this vector, but scaled by 1/scalar.
     */
    public Vector2D divide(double scalar)
    {
        return new Vector2D(x / scalar, y / scalar);
    }

    /**
     * Returns the dot product of this vector and another vector.
     */
    public double dot(Vector2D other)
    {
        return x * other.getX() + y * other.getY();
    }

    /**
     * Returns the length of this vector.
     */
    public double length()
    {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Returns a new, normalized version of this vector, ie. a vector with the same direction as this vector, but with length 1.
     */
    public Vector2D normalize()
    {
        return divide(length());
    }

    /**
     * Performs a linear transformation on this vector.
     */
    public Vector2D transform(Vector2D i, Vector2D j)
    {
        return new Vector2D(x * i.getX() + y * i.getY(), x * j.getX() + y * j.getY());
    }

    /**
     * Returns a new vector with a direction that is rotate counter-clockwise by the angle specified.
     */
    public Vector2D rotate(double angle)
    {

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        // This is a linear transformation with a rotation matrix. This can also be done using transform()
        return new Vector2D(x * cos - y * sin, x * sin + y * cos);
    }

    /**
     * Returns the distance between two points.
     */
    public double distance(Vector2D other)
    {
        return subtract(other).length();
    }

    /**
     * Returns the squared distance between two points.
     */
    public double distanceSquared(Vector2D other)
    {
        return subtract(other).lengthSquared();
    }

    /**
     * Returns the squared length of the vector.
     */
    public double lengthSquared()
    {
        return x * x + y * y;
    }

    @Override
    public String toString()
    {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(vector2D.x, x) == 0 && Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    public IntVector2D toIntVector2D()
    {
        return new IntVector2D((int) x, (int) y);
    }
}
