// userSchema.js  
import * as yup from 'yup';  

export const userSchema = yup.object().shape({  
  firstName: yup.string().required('First name is required'),  
  lastName: yup.string().required('Last name is required'),  
  phone: yup   
    .string()  
    .matches(/^\d{3}-\d{3}-\d{4}$/, 'Phone number must be in the format 444-555-8888')  
    .required('Phone is required'),  
  email: yup.string().email('Must be a valid email').required('Email is required'),  
  role: yup   
    .string()  
    .oneOf(['Admin','Manager','Customer'], 'Invalid role')  
    .required('Role is required'),  
});