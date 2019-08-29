package poc.kafka;

import org.apache.commons.lang3.SerializationUtils;

import poc.kafka.domain.Animal;
import poc.kafka.domain.Animal2;
import poc.kafka.domain.Cat;
import poc.kafka.domain.Dog;
import poc.kafka.domain.Dog2;
import poc.kafka.domain.serialization.AnimalDeserializer2;
import poc.kafka.domain.serialization.AnimalSerializer2;

public class Test {

	public static void main(String[] args) {
		Animal2 a2 = new Dog2(4);
		System.out.println("a2: " + a2);

		byte[] a2Arr = SerializationUtils.serialize(a2);
		Animal2 a22 = SerializationUtils.deserialize(a2Arr);
		System.out.println("a22: " + a22);

		Animal dog = new Dog();
		// dog.whoAmI();

		Animal cat = new Cat();
		// cat.whoAmI();

		Animal animal = new Animal();
		// animal.whoAmI();

		Dog d = new Dog(4);

		// d.whoAmI();

		byte[] dogBytes = new AnimalSerializer2().serialize("", dog);
		System.out.println("dogBytes: " + dogBytes);

		Animal a = new AnimalDeserializer2().deserialize("", dogBytes);

		System.out.println("a: " + a);

		if (a instanceof Animal) {
			a.whoAmI();
		}

		if (a instanceof Dog) {
			((Dog) a).whoAmI();
		}

//		if (a instanceof Cat) {
//			((Cat) a).whoAmI();
//		}

		byte[] bArr = SerializationUtils.serialize(d);
		System.out.println("bArr: " + bArr);

		Dog d2 = SerializationUtils.deserialize(bArr);
		System.out.println("d2: " + d2);
		d2.whoAmI();

	}

}
