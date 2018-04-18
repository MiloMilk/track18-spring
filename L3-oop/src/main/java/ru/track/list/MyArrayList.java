package ru.track.list;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    private int[] array;
    private int current_node;
    private int capacity;
    private int lenght = 0;

    public MyArrayList() {
        capacity = 0;
        current_node = -1;
        array = new int[0];
    }

    public MyArrayList(int capacity) {
        this.capacity = capacity;
        current_node = -1;
        array = new int[capacity];
    }

    @Override
    void add(int item) {
        lenght++;
        if (capacity!=0){
            array[++current_node] = item;
            capacity--;
        } else {
            int[] new_array = new int[array.length + 1];
            System.arraycopy(array,0, new_array , 0, array.length);
            new_array[++current_node] = item;
            array = new_array;
            capacity = 0;
        }
     }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if ((idx>=0)&(idx < array.length)) {

            int ret = array[idx];

            int i = idx;
            if (i < array.length - 1){
                while (i < array.length - 1){
                    array[i]=array[i+1];
                    i++;
                }
            }


            capacity++;
            lenght--;
            return ret;

        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        int ret;
        if ((idx>=0)&(idx<array.length)) {
            ret = array[idx];
            return ret;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    int size() {
        return lenght;
    }
}
