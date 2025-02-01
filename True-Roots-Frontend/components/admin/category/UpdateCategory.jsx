'use client';

import styles from '@/styles/components/admin/update-category/update-category.module.scss';
import Properties from './UpdateProperties';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { swAlert } from '@/helpers/swal';
import { updateCategoryAction } from '@/actions/category-action';
import { convertFormDataToJSON } from '@/helpers/form-validation';

export default function UpdateCategory({ data }) {
  const router = useRouter();

  // Varsayılan değerler
  const [title, setTitle] = useState();
  const [slug, setSlug] = useState();
  const [icon, setIcon] = useState();
  const [seq, setSeq] = useState();
  const [isActive, setIsActive] = useState();
  const [propertyKeys, setProperties] = useState([]);
  const { id } = data;
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearchTerm = (query) => {
    setSearchTerm(query);
  };

  useEffect(() => {
    setTitle(data?.title || '');
    setSlug(data?.slug || '');
    setIcon(data?.icon || '');
    setSeq(data?.seq || 0);
    setIsActive(data?.active || false);
    setProperties(data?.categoryPropertyKeys || []);
  }, [data]);


  const toggleStatus = (e) => {
    e.preventDefault();
    setIsActive((prevStatus) => !prevStatus);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const formData = new FormData(event.target.closest('form'));
    const fields = convertFormDataToJSON(formData);

    // Use the current propertyKeys from the child component
    const categoryData = {
      title: fields.title,
      slug: fields.slug,
      icon: fields.icon,
      seq: fields.seq,
      active: isActive
    };

    try {
      const response = await updateCategoryAction(id, categoryData);

      if (response.success) {
        swAlert(response.message, 'success');
        router.push('/admin-dashboard/admin-categories');
      } else {
        swAlert(response.message || 'Failed to update category', 'error');
      }
    } catch (error) {
      swAlert('An error occurred while updating the category', 'error');
    }
  };

  const handleBack = () => {
    router.back(); // Önceki sayfaya dön
  };

  return (
    <main className={styles.container}>
      <div className={styles.form_container}>
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.fields_container}>
            <div className={styles.title}>
              <label htmlFor="title">Title</label>
              <input
                type="text"
                id="title"
                name="title"
                defaultValue={title}
                onChange={(e) => setTitle(e.target.value)}
              />
            </div>
            <div className={styles.slug}>
              <label htmlFor="slug">Slug</label>
              <input
                type="text"
                id="slug"
                name="slug"
                defaultValue={slug}
                onChange={(e) => setSlug(e.target.value)}
              />
            </div>
            <div className={styles.multiple_field}>
              <div className={styles.icon}>
                <div>
                  <label htmlFor="icon">Icon</label>
                  <input
                    type="icon"
                    id="icon"
                    name="icon"
                    placeholder="icon"
                    defaultValue={icon}
                    onChange={(e) => setIcon(e.target.value)}
                  />
                </div>

              </div>
              <div className={styles.sequence}>
                <label htmlFor="seq">Sequence</label>
                <input type="number" id="seq" name="seq" defaultValue={seq}/>
              </div>
              <div className={styles.status}>
                <span>Status</span>
                <div className={styles.button_active}>
                  <button
                    className={styles.active_button}
                    onClick={toggleStatus}
                    style={{ backgroundColor: isActive ? 'green' : 'gray' }}
                  >
                    <div
                      className={styles.status_circle}
                      style={{ left: isActive ? '28px' : '-4px' }}
                    ></div>
                  </button>
                  <span className={styles.isActive_span}>
                    {isActive ? 'Active' : 'Inactive'}
                  </span>
                </div>
              </div>
            </div>
           
            <div className={styles.create_container}>
              <button
                className={styles.create_button}
                type="button"
                onClick={handleBack}
              >
                Back
              </button>
              <button className={styles.create_button} type="submit">
                Save
              </button>
            </div>
          </div>
        </form>
      </div>

      <div className={styles.properties_container}>
        <Properties categoryPropertyKeys={propertyKeys} categoryId={id} />
      </div>
    </main>
  );
}
