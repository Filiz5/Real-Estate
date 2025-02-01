"use server";

import { Slogan } from '@/components/common/footer/slogan';
import {
  YupValidationError,
  convertFormDataToJSON,
  response,
  transformYupErrors
} from '@/helpers/form-validation';
import { ChangePasswordSchema, UpdateUserSchema } from '@/helpers/schemas/user-schema';
import { swConfirm } from '@/helpers/swal';
import { register } from '@/services/auth-service';
import { changePassword, deleteUserAccount, updateUser } from '@/services/user-service';
import { AuthError } from 'next-auth';
import { revalidatePath } from 'next/cache';


export const updateUserAction = async (prevState, formData) => {
  const fields = convertFormDataToJSON(formData);  
  
  try {
    UpdateUserSchema.validateSync(fields, { abortEarly: false });
    
    const res = await updateUser(fields);
    const data = await res.json();
    if (res.ok) {
      return response(true, data.message, {}, data.object);
    } else {
      return response(false, data.message);
    }
  } catch (err) {
    if (err instanceof YupValidationError) {
      return transformYupErrors(err.inner);
    } else if (err instanceof AuthError) {
      return response(false, err.message);
    } else {
      return response(false, err.message);
    }
  }
};

export const deleteUserAccountAction = async () => {
    try {
        const res = await deleteUserAccount();
        const data = await res.json();
        if (!res.ok){
            return response(false, data.message);
        }
        return response(true, data.message);
    }catch (err){
         if (err instanceof AuthError) {
           return response(false, err.message);
         } else {
           return response(false, err.message);
         }
    }
}

export const changePasswordAction = async (prevState, formData) => {
  const fields = convertFormDataToJSON(formData);
  try {
    await ChangePasswordSchema.validate(fields, { abortEarly: false });
    const payload = {
      oldPassword: fields.oldPassword,
      newPassword: fields.newPassword
    };
    const data = await changePassword(payload);
    
    if (!data.success) {
      return response(false, data.message);
    } else {
      return response(true, data.message);
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
