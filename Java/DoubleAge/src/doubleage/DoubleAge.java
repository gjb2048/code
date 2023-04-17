/*
 * Double Age.
 *
 * Copyright (C) 2023 G J Barnard.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later.
 */

package doubleage;

import java.util.GregorianCalendar;

/**
 * Double Age class.
 *
 * @copyright 2023 G J Barnard.
 */
public class DoubleAge {

    private final GregorianCalendar ageOne = new GregorianCalendar();
    private final GregorianCalendar ageTwo = new GregorianCalendar();
    private final GregorianCalendar current = new GregorianCalendar();
    
    private final int maxYears = 20;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DoubleAge us = new DoubleAge();
        
        us.ages();
    }
    
    public DoubleAge() {
        this.ageOne.set(1968, 1, 24); // Birthday of older person.
        this.ageTwo.set(1980, 1, 12); // Birthday of younger person.
        this.current.set(1980, 1, 12); // Start point being the birthday of the younger person.
    }
    
    public void ages() {
        int yearCount = 0;
        int oneAge;
        int twoAge;
        int dble;
        
        while (yearCount < this.maxYears) {
            oneAge = this.ourAge(this.ageOne);
            twoAge = this.ourAge(this.ageTwo);
            System.out.print("One's age: " + oneAge);
            System.out.print(" Two's age: " + twoAge);
            dble = twoAge * 2;
            System.out.print(" Double two's age: " + dble + " - ");
            if (dble == oneAge) {
                System.out.println("Double!");
            } else if (dble < oneAge) {
                System.out.println((oneAge - dble) + " years more than double");
            } else if (dble > oneAge) {
                System.out.println((dble - oneAge) + " years less than double");
            }
            this.current.add(GregorianCalendar.YEAR, 1);
            
            yearCount++;
        }
    }
    
    private int ourAge(GregorianCalendar theOne) {
        int ourAge = this.current.get(GregorianCalendar.YEAR) - theOne.get(GregorianCalendar.YEAR);
        if (this.current.get(GregorianCalendar.DAY_OF_YEAR) < theOne.get(GregorianCalendar.DAY_OF_YEAR)) {
            ourAge--;
        }
        return ourAge;
    }
}
