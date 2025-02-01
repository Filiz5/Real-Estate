'use client';
import React, { useEffect, useState } from 'react';
import { FaTrash, FaEdit, FaCheck } from 'react-icons/fa';
import styles from '@/styles/components/admin/category/admin-category-table.module.scss';
import { useRouter } from 'next/navigation';
import  SearchBoxCategory  from './search-box-category';
import SearchBoxAdvertType from '../advert-type/advert-type-search-box';
import { deleteCategory, deleteCategoryAction } from '@/actions/category-action';
import Swal from 'sweetalert2';
import { swAlert } from '@/helpers/swal';
import NoDateFound from '@/components/common/NoDateFound';
import Paginations from './category-pagination';

export default function CategoryTable({ data, currentPage, searchParams }) {
  const [loading, setLoading] = useState(true); // Yüklenme durumu
  const router = useRouter();
  const { content } = data;
  const [searchTerm, setSearchTerm] = useState('');
  const [category, setCategory] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const url = new URL(window.location.href);
    if (searchTerm) url.searchParams.set('q', searchTerm);
    else url.searchParams.delete('q');
    setFilteredData(data?.content || []);
    setTotalPages(data?.totalPages || 0);
    router.push(url.toString());
  }, [searchTerm, data, router, content]);

  // useEffect(() => {
  //   const waitmethod = async () => {
  //     await wait(4000);
  //     setLoading(false);
  //   };
  //   waitmethod();
  // }, []);

  const handleEdit = (id) => {
    if (!id) {
      console.error('Invalid ID');
      return;
    }
    router.push(`/admin-dashboard/admin-categories/${id}`);
  };

  const handleAddNew = () => {
    router.push('/admin-dashboard/admin-categories/new');
  };

  const handleSearchTerm = (query) => {
    setSearchTerm(query);
  };

  const handleDelete = async (id) => {
    try {
      const result = await Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
      });

      if (!result.isConfirmed) {
        return;
      }
      const res = await deleteCategoryAction(id);
      if (res.success) {
        swAlert(res.message, 'success');
        router.refresh();
      } else {
        swAlert(res.message, 'error');
      }
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: error.message
      });
    }
  };

  return (
    <>
      <div className={styles.container}>
        <SearchBoxCategory
          data={setCategory}
          handleAddNew={handleAddNew}
          handleSearchTerm={handleSearchTerm}
        />
        {!filteredData || filteredData.length === 0 ? (
          <NoDateFound text={'No Categories Found'} />
        ) : (
          <div className={styles.tableContainer}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Icon</th>
                  <th>Name</th>
                  <th>Sequence</th>
                  <th>Active</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {filteredData.map((category) => (
                  <tr key={category.id}>
                    <td>{category.icon || '🏠'}</td>
                    <td>{category.title}</td>
                    <td>{category.seq}</td>
                    <td>{category.active ? <FaCheck /> : '-'}</td>
                    <td>
                      <button
                        aria-label="Delete"
                        className={styles.actionButton}
                        onClick={() => handleDelete(category.id)}
                      >
                        <FaTrash color="red" />
                      </button>
                      <button
                        aria-label="Edit"
                        className={styles.actionButton}
                        onClick={() => handleEdit(category.id)}
                      >
                        <FaEdit color="purple" />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </>
  );
};

// export default CategoryTable;
