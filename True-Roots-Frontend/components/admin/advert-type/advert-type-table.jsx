'use client';

import React, { useEffect, useState } from 'react';
import { FaEdit, FaTrash } from 'react-icons/fa';
import { deleteAdvertType } from '@/actions/advert-type-actions';
import Swal from 'sweetalert2';
import styles from '@/styles/components/admin/category/admin-category-table.module.scss';
import NoDateFound from '@/components/common/NoDateFound';
import { useRouter } from 'next/navigation';

export default function AdvertTypeTable({ filteredAdvertTypes, handleEdit }) {
  const [filteredData, setFilteredData] = useState([]);
  const router = useRouter();

  useEffect(() => {
    if (filteredAdvertTypes) {
      setFilteredData(filteredAdvertTypes);
    }
  }, [filteredAdvertTypes]);

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

      if (result.isConfirmed) {
        await deleteAdvertType(id);
        Swal.fire({
          title: 'Deleted!',
          text: 'Advert type has been deleted.',
          icon: 'success'
        }).then(() => {
          router.refresh();
        });
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
    <div className={styles.tableContainer}>
      <table className={styles.table}>
        <thead>
          <tr>
            <th className={styles.tableHeader}>Title</th>
            <th className={`${styles.tableHeader} ${styles.actionHeader}`}>
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {filteredData.map((type) => (
            <tr key={type.id} className={styles.tableRow}>
              <td className={styles.tableCell}>{type.title}</td>
              <td className={`${styles.tableCell} ${styles.actionCell}`}>
                <button
                  aria-label="Delete"
                  className={styles.actionButton}
                  onClick={() => handleDelete(type.id)}
                  disabled={type.builtIn}
                >
                  <FaTrash color={type.builtIn ? 'gray' : 'red'} />
                </button>
                <button
                  aria-label="Edit"
                  className={styles.actionButton}
                  onClick={() => handleEdit(type.id)}
                  disabled={type.builtIn}
                >
                  <FaEdit color={type.builtIn ? 'gray' : 'purple'} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
