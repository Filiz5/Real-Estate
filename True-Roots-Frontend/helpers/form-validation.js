import { ValidationError, date, string, object } from 'yup';

export const response = (ok, message, errors, data) => ({
    ok,
    message,
    errors,
    data
});

export const initialResponse = response(null, '', {}, {});

export const convertFormDataToJSON = (formData) => {
    return Object.fromEntries(formData.entries());
};

export const transformYupErrors = (errors) => {
    const errObject = {};
    errors.forEach((error) => (errObject[error.path] = error.message));

    return response(false, '', errObject);
};

export const YupValidationError = ValidationError;

export const isStringArray = (str) => {
    const arr = JSON.parse(str);
    return Array.isArray(arr) && arr.length > 0;
};

export const tourRequestSchema = object({
  date: date().typeError('Invalid date!'),
  time: string()
    .matches(/^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/, 'Invalid time!')
});
