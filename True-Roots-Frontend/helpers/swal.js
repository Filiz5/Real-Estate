import Swal from "sweetalert2";

export const swAlert = (title, icon = 'info', text = '', timer = 4000) => {
    Swal.fire({
        title,
        text,
        icon,
        backdrop: true, // This keeps the backdrop enabled
        timer, // This will automatically close the popup after 'timer' milliseconds
        timerProgressBar: true, // Optional: Shows a timer progress bar
        showConfirmButton: false // Optional: Hides the confirm button to avoid confusion
    });
};

export const swConfirm = (
    title,
    icon = 'info',
    text = '',
    confirmButtonText = 'Yes',
    cancelButtonText = 'Cancel'
) => {
    return Swal.fire({
        title,
        text,
        icon,
        showCancelButton: true,
        confirmButtonText,
        cancelButtonText
    });
};

export const swToast = (title, icon = "info") => {
	const Toast = Swal.mixin({
		toast: true,
		position: "top-end",
		showConfirmButton: false,
		timer: 3000,
		timerProgressBar: true,
		didOpen: (toast) => {
			toast.onmouseenter = Swal.stopTimer;
			toast.onmouseleave = Swal.resumeTimer;
		},
	});
	Toast.fire({
		icon,
		title,
	});
};
