'use client';

import {
  addPropertyAction,
  getPropertiesAction,
  updatePropertyAction,
  deletePropertyAction
} from '@/actions/category-action';
import { swAlert } from '@/helpers/swal';
import styles from '@/styles/components/admin/update-category/update-properites.module.scss';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { CiEdit } from 'react-icons/ci';
import { IoTrashBinOutline } from 'react-icons/io5';
import { IoIosAddCircleOutline } from 'react-icons/io';

export default function Properties({ categoryPropertyKeys, categoryId }) {
  const [propertyKeys, setPropertyKeys] = useState([]);
  const [propertyField, setPropertyField] = useState(null);
  const [updatedProperty, setUpdatedProperty] = useState({});
  const [error, setError] = useState({ addFiled: '', updateFiled: '' });
  const router = useRouter();

  useEffect(() => {}, [categoryPropertyKeys]);

  useEffect(() => {
    const fetchProperties = async () => {
      const properties = await getPropertiesAction(categoryId);
      setPropertyKeys(properties);
    };
    fetchProperties();
  }, [categoryId, categoryPropertyKeys]);

  const handlePropertyChange = (id, value) => {
    setPropertyKeys((prevProperties) =>
      prevProperties.map((property) =>
        property.id === id ? { ...property, name: value } : property
      )
    );
    setUpdatedProperty({ id, name: value });
  };

  const updateProperty = async () => {
    if (!updatedProperty.name || updatedProperty.name.trim() === '') {
      setError({ updateFiled: 'Property field cannot be empty' });
      return;
    }
    const properyData = { name: updatedProperty.name };
    const res = await updatePropertyAction(updatedProperty.id, properyData);
    if (res.success) {
      swAlert(res.message, 'success');
      router.refresh();
    } else {
      swAlert(res.message, 'error');
    }
  };

  const addProperty = async (e) => {
    e.preventDefault();
    if (!propertyField || propertyField.trim() === '') {
      setError({ addFiled: 'Property field cannot be empty' });
      return;
    }
    const propertyData = { name: propertyField };

    const res = await addPropertyAction(propertyData, categoryId);
    if (res.success) {
      swAlert(res.message, 'success');
      setPropertyField('');
      router.refresh();
    } else {
      setError(res.message);
    }
  };

  const deleteProperty = async (id) => {
    const res = await deletePropertyAction(id);
    if (res.success) {
      swAlert(res.message, 'success');
      router.refresh();
    } else {
      swAlert(res.message, 'error');
    }
  };

  return (
    <div className={styles.container}>
      <h3>Properties</h3>
      <div className={styles.outer_container}>
        <div className={styles.add_container}>
          <div className={styles.add_property}>
            <input
              type="text"
              className={styles.property_input}
              value={propertyField}
              required
              onChange={(e) => setPropertyField(e.target.value)}
            />
            <button
              title="Add property field"
              type="submit"
              className={styles.add_button}
              onClick={addProperty}
            >
              <IoIosAddCircleOutline size={30} />
            </button>
          </div>
          {error.addFiled && (
            <span className={styles.error}>{error.addFiled}</span>
          )}
        </div>

        <div className={styles.inner_container}>
          {propertyKeys.length > 0 &&
            propertyKeys.map((property) => (
              <div key={property.id} className={styles.property}>
                <input
                  type="text"
                  value={property.name}
                  onChange={(e) =>
                    handlePropertyChange(property.id, e.target.value)
                  }
                />
                {propertyKeys.length > 1 && (
                  <div className={styles.buttons}>
                    <button
                      title="Edit property field"
                      className={styles.edit_button}
                      onClick={updateProperty}
                    >
                      <CiEdit />
                    </button>
                    <button
                      title="Delete property field"
                      className={styles.delete_button}
                      onClick={() => deleteProperty(property.id)}
                    >
                      <IoTrashBinOutline />
                    </button>
                  </div>
                )}
              </div>
            ))}
        </div>
      </div>
      {error.updateFiled && (
        <span className={styles.error}>{error.updateFiled}</span>
      )}
    </div>
  );
}
