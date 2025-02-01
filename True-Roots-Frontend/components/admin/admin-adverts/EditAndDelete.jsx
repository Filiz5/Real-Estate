"use client";

import { swAlert, swConfirm } from '@/helpers/swal';
import { deleteAdvert } from '@/services/advert-service';
import styles from '@/styles/components/admin/admin-adverts/edit-and-delete.module.scss';
import { useRouter } from 'next/navigation';
import { CiEdit } from 'react-icons/ci';
import { IoTrashBinOutline } from 'react-icons/io5';

export default function EditAndDelete({advertId}) {
    const router = useRouter();

    const handleEditClick = (id) => {
        if(id) {
            router.push(`/admin-dashboard/admin-adverts/edit/${id}`);
        }
    }

    const handleDelete = async (id) => {
        
        const answer = await swConfirm('Are you sure you want to delete this advert?');
        if(!answer.isConfirmed) return;

        const res = await deleteAdvert(id);

        if(res.success) {
            swAlert(res.message, 'success');
            router.refresh();
        }else{
            swAlert(res.message, 'error');
        }
    }
            


  return (
    <div className={styles.container}>
      <div className={styles.edit_delete_container}>
        <button
            type='button'
          className={styles.edit_button}
          onClick={() => handleEditClick(advertId)}
        >
          <CiEdit size={25} />
        </button>
        <button
            type='button'
          className={styles.delete_button}
          onClick={() => handleDelete(advertId)}
        >
          <IoTrashBinOutline size={25} />
        </button>
      </div>
    </div>
  );
}
