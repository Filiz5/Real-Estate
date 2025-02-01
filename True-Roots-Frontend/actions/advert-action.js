

import {
  YupValidationError,
  convertFormDataToJSON,
  response,
  transformYupErrors
} from '@/helpers/form-validation';
import { createAdvert, updateAdvert, updateStatus, uploadImage } from '@/services/advert-service';
import { AuthError } from 'next-auth';
import { CreateAdvertSchema, UpdateAdvertSchema } from '@/helpers/schemas/advert-schemas';


export const createAdvertAction = async (state, formData) => {

  try {
    await CreateAdvertSchema.validate(formData, { abortEarly: false });
    const data = await createAdvert(formData);
    if (data.ok) {
      return response(true, data.message);
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


export const uploadImageAction = async (formData, advertId) => {

  try {
    const res = await uploadImage(formData, advertId);
    
    if (!res.success) {
      return { success: false, message: res.message };
    }else{
      return { success: true, message: res.message };
    }
    
  }
  catch (err) {
    if (err instanceof YupValidationError) {
      return transformYupErrors(err.inner);
    } else if (err instanceof AuthError) {
      return response(false, err.message);
    }
    throw err;
  }
}

export const UpdateAdvertAction = async (state, formData) => {
  const advertId = { advertId: formData.advertId };
  delete formData.advertId;
  try {
    await UpdateAdvertSchema.validate(formData, { abortEarly: false });
    const data = await updateAdvert(formData, advertId.advertId);
    if (data.ok) {
      return response(true, data.message);
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

export const UpdateStatusAction = async (id, formData) => {
  
  try {
    const message = await updateStatus(id, formData);
    
      return {success: true, message: message};
  } catch (err) {
    throw err;
  }
};