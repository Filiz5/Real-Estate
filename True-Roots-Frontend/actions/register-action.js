"use server";

import {
  YupValidationError,
  convertFormDataToJSON,
  response,
  transformYupErrors
} from '@/helpers/form-validation';
import { RegisterSchema } from '@/helpers/schemas/user-schema';
import { register } from '@/services/auth-service';
import { AuthError } from 'next-auth';

export const registerAction = async (prevState, formData) => {
  const fields = convertFormDataToJSON(formData);
  try {
    RegisterSchema.validateSync(fields, { abortEarly: false });
    const { confirmPassword, ...dataToSend } = fields;
    const res = await register(dataToSend);
    const data = await res.json();
    if (res.ok) {
      return response(true, data.message, data);
    } else {
      return response(false, data.message);
    }
  } catch (err) {
    if (err instanceof YupValidationError) {
      return transformYupErrors(err.inner);
    } else if (err instanceof AuthError) {
      return response(false, err.message);
    } 
    throw err;
  }
};
