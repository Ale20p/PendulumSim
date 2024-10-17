package org.example.pendulumsin;

public class Equations {
    private double length;
    private double gravity;
    private double amplitude;
    private double angularFrequency;
    private double time;
    private double mass;

    /**
     * Calculates the period of a simple pendulum.
     * @param length Length of the pendulum (in meters).
     * @param gravity Acceleration due to gravity (in m/s^2).
     * @return Period of the pendulum (in seconds).
     */
    public static double calculatePeriod(double length, double gravity) {
        return 2 * Math.PI * Math.sqrt(length / gravity);
    }

    /**
     * Calculates the angular frequency of a simple pendulum.
     * @param length Length of the pendulum (in meters).
     * @param gravity Acceleration due to gravity (in m/s^2).
     * @return Angular frequency (in radians per second).
     */
    public static double calculateAngularFrequency(double length, double gravity) {
        return Math.sqrt(gravity / length);
    }

    /**
     * Calculates the angular displacement at a given time.
     * @param amplitude Maximum angular displacement (in radians).
     * @param angularFrequency Angular frequency (in radians per second).
     * @param time Time elapsed (in seconds).
     * @return Angular displacement at the given time (in radians).
     */
    public static double calculateDisplacement(double amplitude, double angularFrequency, double time) {
        return amplitude * Math.cos(angularFrequency * time);
    }

    /**
     * Calculates the angular velocity at a given time.
     * @param amplitude Maximum angular displacement (in radians).
     * @param angularFrequency Angular frequency (in radians per second).
     * @param time Time elapsed (in seconds).
     * @return Angular velocity at the given time (in radians per second).
     */
    public static double calculateVelocity(double amplitude, double angularFrequency, double time) {
        return -amplitude * angularFrequency * Math.sin(angularFrequency * time);
    }

    /**
     * Calculates the angular acceleration at a given time.
     * @param amplitude Maximum angular displacement (in radians).
     * @param angularFrequency Angular frequency (in radians per second).
     * @param time Time elapsed (in seconds).
     * @return Angular acceleration at the given time (in radians per second squared).
     */
    public static double calculateAcceleration(double amplitude, double angularFrequency, double time) {
        return -amplitude * Math.pow(angularFrequency, 2) * Math.cos(angularFrequency * time);
    }

    /**
     * Calculates the kinetic energy of the pendulum at a given time.
     * @param mass Mass of the pendulum bob (in kilograms).
     * @param length Length of the pendulum (in meters).
     * @param amplitude Maximum angular displacement (in radians).
     * @param angularFrequency Angular frequency (in radians per second).
     * @param time Time elapsed (in seconds).
     * @return Kinetic energy at the given time (in joules).
     */
    public static double calculateKineticEnergy(double mass, double length, double amplitude, double angularFrequency, double time) {
        double angularVelocity = calculateVelocity(amplitude, angularFrequency, time);
        double tangentialVelocity = length * angularVelocity;
        return 0.5 * mass * Math.pow(tangentialVelocity, 2);
    }

    /**
     * Calculates the potential energy of the pendulum at a given time.
     * @param mass Mass of the pendulum bob (in kilograms).
     * @param length Length of the pendulum (in meters).
     * @param amplitude Maximum angular displacement (in radians).
     * @param angularFrequency Angular frequency (in radians per second).
     * @param time Time elapsed (in seconds).
     * @param gravity Acceleration due to gravity (in m/s^2).
     * @return Potential energy at the given time (in joules).
     */
    public static double calculatePotentialEnergy(double mass, double length, double amplitude, double angularFrequency, double time, double gravity) {
        double angularDisplacement = calculateDisplacement(amplitude, angularFrequency, time);
        double height = length * (1 - Math.cos(angularDisplacement)); // Vertical displacement from lowest point
        return mass * gravity * height;
    }
}
