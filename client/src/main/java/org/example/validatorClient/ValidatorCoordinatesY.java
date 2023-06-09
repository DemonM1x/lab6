package org.example.validatorClient;



public class ValidatorCoordinatesY extends ValidateAbstract<Integer> {

    public ValidatorCoordinatesY() {
        super("City.Coordinates.y" , "");
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public boolean validate(String value) {
        Integer val;
        try {
            val = Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validate(String[] value) {
        return false;
    }
}
