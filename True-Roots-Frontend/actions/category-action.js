'use server';

import {
  YupValidationError,
  response,
  transformYupErrors
} from '@/helpers/form-validation';
import categorySchema from '@/helpers/schemas/category-schema';
import {
  addProperty,
  createCategory,
  deleteCategory,
  deleteProperty,
  getProperties,
  updateCategory,
  updateProperty
} from '@/services/category-service';
import { AuthError } from 'next-auth';
import { revalidatePath } from 'next/cache';

export const createCategoryAction = async (state, formData) => {
  try {
    await categorySchema.validate(formData, { abortEarly: false });
    const data = await createCategory(formData);
    if (data.success) {
      
      return response(true, data.data.message);
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

export async function deleteCategoryAction(id) {
  try {
    const response = await deleteCategory(id);

    if (!response) {
      return { success: false, message: 'Error deleting category' };
    }

    return { success: true, message: 'Category deleted successfully' };
  } catch (error) {
    console.error('Error deleting category:', error.message);
    throw error;
  }
}

export async function updateCategoryAction(id, formData) {
  try {
    const response = await updateCategory(id, formData);

    if (!response.success) {
      return { success: false, message: data.message };
    }

    return { success: true, message: response.message };
  } catch (error) {
    throw error;
  }
}

export async function getPropertiesAction(categoryId) {
  try {
    const res = await getProperties(categoryId);
    if (!res.success) {
      return { success: false, message: res.message };
    } else {
      return res.data;
    }
  } catch (error) {
    throw error;
  }
}

export async function addPropertyAction(formData, categoryId) {
  try {
    const response = await addProperty(formData, categoryId);
    if (response.success) {
      return { success: true, message: response.message };
    } else {
      return { success: false, message: response.message };
    }
  } catch (error) {
    throw error;
  }
}

export const deletePropertyAction = async (id) => {
  try {
    const response = await deleteProperty(id);
    if (!response.success) {
      return { success: false, message: response.message };
    }
    return { success: true, message: response.message };
  } catch (error) {
    throw error;
  }
};

export async function updatePropertyAction(id, formData) {
  try {
    const response = await updateProperty(id, formData);
    if (!response.success) {
      return { success: false, message: response.message };
    }
    return { success: true, message: response.message };
  } catch (error) {
    throw error;
  }
}
