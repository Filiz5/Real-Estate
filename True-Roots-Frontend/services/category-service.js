"use server";

import { getAuthHeader } from '@/helpers/auth-helper';

import { config } from '@/helpers/config';

const API_URL = config.api.baseUrl;

export const getCategories = async (searchTerm, currentPage=0, pageSize, sort, type) => {
  const qs = `q=${searchTerm}&page=${currentPage}&size=${pageSize}&sort=${sort}&type=${type}`;

  try {
    const response = await fetch(`${API_URL}/categories/admin?${qs}	`, {
      method: "GET",
      headers: await getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch category: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error("Error fetching categories:", error.message);
    throw error;
  }
}; 

export const getCategoriesAsList = async () => {
  try {
    const response = await fetch(`${API_URL}/categories/list`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    if (!response.ok) {
      throw new Error(`Failed to fetch category: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error("Error fetching categories:", error.message);
    throw error;
  }
}

export const createCategory = async (formData) => {
  try {
    const response = await fetch(`${API_URL}/categories`, {
      method: 'POST',
      headers: await getAuthHeader(),
      body: JSON.stringify(formData),
    });

    const data = await response.json();

    if (!response.ok) {
      return {success: false, message: data.message}
    }
    
    return {success: true, data: data};
  } catch (error) {
    console.error("Error creating category:", error.message);
    throw new Error("Failed to create category");
  }
};

export const getCategoryById = async (id) => {
  try {
    const response = await fetch(`${API_URL}/categories/${id}`, {
      method: 'GET',
      headers: await getAuthHeader(),
    });

    if (!response.ok) {
      throw new Error(`Failed to fetch category: ${response.statusText}`);
    }

    const data = await response.json();
    return data.object;
  } catch (error) {
    console.error("Error fetching category by ID:", error.message);
    throw error;
  }
};

export async function updateCategory  (id, formData)  {
      try {    
        const response = await fetch(`${API_URL}/categories/${id}`, {
          method: 'PUT',
          headers: await getAuthHeader(),
          body: JSON.stringify(formData),
        });
    
        const data = await response.json();
    
        if (!response.ok) {
          return { success: false, message: data.message };
        }
    
        return { success: true, message: data.message };
      } catch (error) {
        throw error;
      }
    };

export const getProperties = async (id) => {
  try {
    const response = await fetch(`${API_URL}/categories/${id}/properties`, {
      method: 'GET',
      headers: await getAuthHeader(),
    });

    const data = await response.json();

    if (!response.ok) {
      return { success: false, message: "Failed to fetch properties" };
    }else{
      return { success: true, data: data.object };
    }
  } catch (error) {
    throw error;
  }
}

    export async function addProperty  (formData, id)  {
      try {    
        const res = await fetch(`${API_URL}/categories/${id}/properties`, {
          method: 'POST',
          headers: await getAuthHeader(),
          body: JSON.stringify(formData),
        });
        const data = await res.json();
    
        if (!res.ok) {
          return { success: false, message: data.message };
        }
    
        return { success: true, message: data.message };
      } catch (error) {
        throw error;
      }
    };

    export async function deleteCategory(id) {
          try {
        
            const response = await fetch(`${API_URL}/categories/${id}`, {
              method: "DELETE",
              headers: await getAuthHeader()
            });
        
            if (!response.ok) {
              const errorData = await response.json();
              throw new Error(errorData.message || `Failed to delete category: ${response.statusText}`);
            }
        
            return true;
          } catch (error) {
            console.error("Error deleting category:", error.message);
            throw error;
          }
        }

export async function deleteProperty(id) {
  try {
    const response = await fetch(`${API_URL}/categories/properties/${id}`, {
      method: "DELETE",
      headers: await getAuthHeader()
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || `Failed to delete property: ${response.statusText}`);
    }

    return { success: true, message: "Property deleted successfully" };
  } catch (error) {
    console.error("Error deleting property:", error.message);
    throw error;
  }
}

export async function updateProperty(id, formData) {
  try {
    const response = await fetch(`${API_URL}/categories/properties/${id}`, {
      method: 'PUT',
      headers: await getAuthHeader(),
      body: JSON.stringify(formData),
    });

    const data = await response.json();

    if (!response.ok) {
      return { success: false, message: data.message };
    }

    return { success: true, message: data.message };
  } catch (error) {
    throw error;
  }
}

export async function getCategoriesForHomePage(){
  try{
    const res = await fetch(`${API_URL}/adverts/categories`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });
    const data = await res.json();
    if (!res.ok) {
      throw new Error(`Failed to fetch category: ${data.message}`);
    }
    return data;
  }catch(error){
    throw new Error(`Failed to fetch categories`);
  }
}


