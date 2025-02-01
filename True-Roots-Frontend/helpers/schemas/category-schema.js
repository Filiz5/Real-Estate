import * as Yup from 'yup';

// Define the Yup validation schema
const categorySchema = Yup.object().shape({
  title: Yup.string()
    .required('Title is required')
    .min(3, 'Title must be at least 3 characters')
    .max(50, 'Title must not exceed 50 characters'),

  slug: Yup.string()
    .required('Slug is required'),

  icon: Yup.string().required('Icon is required'),

  seq: Yup.number()
    .typeError('Sequence must be a number')
    .required('Sequence is required')
    .integer('Sequence must be an integer')
    .min(0, 'Sequence must be greater than or equal to 0'),

  status: Yup.boolean().required('Status is required'),

  properties: Yup.array()
    .of(
      Yup.object().shape({
        name: Yup.string()
          .required('Property name is required')
          .min(1, 'Property name cannot be empty')
      })
    )
    .min(1, 'At least one property is required')
});

export default categorySchema;
