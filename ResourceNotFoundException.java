
/**
 * This is a checked exception which is thrown if an ant is looking for a resource but cannot
 * find any in its range.
 * 
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class ResourceNotFoundException extends java.lang.Exception{
  public ResourceNotFoundException() {
    super("Could not find rescource in range.");
  }
}
