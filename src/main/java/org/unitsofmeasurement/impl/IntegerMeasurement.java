package org.unitsofmeasurement.impl;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.measure.Measurement;
import javax.measure.Quantity;
import javax.measure.Unit;

import org.unitsofmeasurement.impl.function.AbstractConverter;

class IntegerMeasurement<T extends Quantity<T>> extends AbstractMeasurement<T> {


	final int value;

    public IntegerMeasurement(int value, Unit<T> unit) {
    	super(unit);
    	this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    // Implements Measurement
    @Override
    public double doubleValue(Unit<T> unit) {
        return (super.unit.equals(unit)) ? value : super.unit.getConverterTo(unit).convert(value);
    }

    // Implements Measurement
    @Override
    public BigDecimal decimalValue(Unit<T> unit, MathContext ctx)
            throws ArithmeticException {
        BigDecimal decimal = BigDecimal.valueOf(value);
        return (super.unit.equals(unit)) ? decimal : ((AbstractConverter)super.unit.getConverterTo(unit)).convert(decimal, ctx);
    }

	@Override
	public long longValue(Unit<T> unit) {
        double result = doubleValue(unit);
        if ((result < Long.MIN_VALUE) || (result > Long.MAX_VALUE)) {
            throw new ArithmeticException("Overflow (" + result + ")");
        }
        return (long) result;
	}

	protected Measurement<T, Number> add(AbstractMeasurement<T> that) {
		return of(value + that.getValue().intValue(), getUnit()); // TODO use shift of the unit?
	}

	protected Measurement<T, Number> subtract(AbstractMeasurement<T> that) {
		return of(value - that.getValue().intValue(), getUnit()); // TODO use shift of the unit?
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Measurement<?, Number> multiply(AbstractMeasurement<T> that) {
		return new IntegerMeasurement(value * that.getValue().intValue(),
				getUnit().multiply(that.getUnit()));
	}

	public Measurement<T, Number> multiply(Number that) {
		return of(value * that.intValue(),
				getUnit().multiply(that.intValue()));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Measurement<?, Number> divide(AbstractMeasurement<T> that) {
		return new DoubleMeasurement(value / that.getValue().doubleValue(), getUnit().divide(that.getUnit()));
	}

	@SuppressWarnings("unchecked")
	public AbstractMeasurement<T> inverse() {
		return (AbstractMeasurement<T>) of(value, getUnit().inverse());
	}

	@Override
	public boolean isBig() {
		return false;
	}

	public Measurement<T, Number> divide(Number that) {
		return of(value / that.intValue(), getUnit());
	}

}