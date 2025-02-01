'use server';

import { signIn } from '@/auth';
import {
  YupValidationError,
  convertFormDataToJSON,
  response,
  transformYupErrors
} from '@/helpers/form-validation';
import { AuthSchema, ForgotPasswordSchema, ResetPasswordSchema } from '@/helpers/schemas/auth-schema';
import { forgotPassword, resetPassword } from '@/services/auth-service';
import { AuthError } from 'next-auth';

export const loginAction = async (state, formData) => {
  const fields = convertFormDataToJSON(formData);
  try {
    AuthSchema.validateSync(fields, { abortEarly: false });
    await signIn('credentials', fields);
  } catch (err) {
    if (err instanceof YupValidationError) {
      return transformYupErrors(err.inner);
    } else if (err instanceof AuthError) {
      return response(false, 'Invalid email or password');
    }
    throw err;
  }
};

export const loginWithGoogleAction = async (state) => {
  try {
    await signIn('google');
  } catch (err) {
    if (err instanceof AuthError) {
      return response(false, 'Unfortunately, we could not log you in with Google');
    }
    throw err;
  }
};

export const forgotPasswordAction = async (state, formData) => {
  const fields = convertFormDataToJSON(formData);
  try {
    ForgotPasswordSchema.validateSync(fields, { abortEarly: false });
    const res = await forgotPassword(fields);
    if (!res.ok) {
      return response(false, res.message);
    }
    return response(true, res.message);
  } catch (err) {
    if (err instanceof YupValidationError) {
      return transformYupErrors(err.inner);
    } else if (err instanceof AuthError) {
      return response(false, 'Invalid email');
    }
    throw err;
  }
}

export const resetPasswordAction = async (state, formData) => {
  const fields = convertFormDataToJSON(formData);
  try {
    ResetPasswordSchema.validateSync(fields, { abortEarly: false });
    const res = await resetPassword(fields);
    if (!res.ok) {
      return response(false, res.message);
    }
    return response(true, res.message);
  } catch (err) {
    if (err instanceof YupValidationError) {
      return transformYupErrors(err.inner);
    } else if (err instanceof AuthError) {
      return response(false, 'Invalid email or password');
    }
    throw err;
  }
};
