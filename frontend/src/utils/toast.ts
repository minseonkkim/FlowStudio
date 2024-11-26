import { Bounce, toast, ToastOptions } from "react-toastify";



export const toastSuccess = (msg: string, options?: ToastOptions) => {
    toast.success(msg, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
        ...options,
    });
}


export const toastError = (msg: string, options?: ToastOptions) => {
    toast.error(msg, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
        theme: "light",
        transition: Bounce,
        ...options,
    });
}